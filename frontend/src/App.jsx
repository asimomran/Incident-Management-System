import React, { useState, useEffect } from 'react';

function RawSignals({ onBack }) {
  const [signals, setSignals] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/signals/raw')
      .then(res => res.json())
      .then(data => setSignals(data))
      .catch(e => console.error('Failed to fetch raw signals', e));
  }, []);

  return (
    <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
      <h1>Raw Signals (Data Lake)</h1>
      <button onClick={onBack}>← Back to Dashboard</button>
      <br /><br />
      <table border="1" cellPadding="10" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ background: '#f4f4f4' }}>
            <th>#</th>
            <th>Component</th>
            <th>Error</th>
            <th>Timestamp</th>
          </tr>
        </thead>
        <tbody>
          {signals.length === 0 ? (
            <tr><td colSpan="4">No signals recorded yet.</td></tr>
          ) : (
            signals.map((s, i) => (
              <tr key={i}>
                <td>{i + 1}</td>
                <td>{s.component}</td>
                <td>{s.error}</td>
                <td>{s.timestamp}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

function App() {
  const [incidents, setIncidents] = useState([]);
  const [selectedIncident, setSelectedIncident] = useState(null);
  const [rcaText, setRcaText] = useState('');
  const [loadingAi, setLoadingAi] = useState(false);
  const [showRawSignals, setShowRawSignals] = useState(false);

  useEffect(() => {
    fetchIncidents();
  }, []);

  const fetchIncidents = async () => {
    try {
      const res = await fetch('/incidents');
      if (res.ok) {
        const data = await res.json();
        setIncidents(data);
      }
    } catch (e) {
      console.error('Failed to fetch incidents', e);
    }
  };

  const updateStatus = async (id, status) => {
    try {
      const res = await fetch(`/incidents/${id}/status?status=${status}`, {
        method: 'PUT',
      });
      if (res.ok) {
        fetchIncidents();
        if (selectedIncident && selectedIncident.id === id) {
          setSelectedIncident({ ...selectedIncident, status });
        }
      }
    } catch (e) {
      console.error('Failed to update status', e);
    }
  };

  const submitRca = async (id) => {
    if (!rcaText) return;
    try {
      const res = await fetch(`/incidents/${id}/rca`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(rcaText),
      });
      if (res.ok) {
        fetchIncidents();
        if (selectedIncident && selectedIncident.id === id) {
          setSelectedIncident({ ...selectedIncident, rca: rcaText });
        }
        setRcaText('');
        alert('RCA submitted successfully!');
      }
    } catch (e) {
      console.error('Failed to submit RCA', e);
    }
  };

  const suggestRca = async () => {
    setLoadingAi(true);
    try {
      const res = await fetch('https://api.adviceslip.com/advice');
      const data = await res.json();
      setRcaText(`AI Suggestion: ${data.slip.advice}. We need to investigate this further.`);
    } catch (e) {
      console.error(e);
      setRcaText('AI Suggestion: The database connection timed out due to high load.');
    } finally {
      setLoadingAi(false);
    }
  };

  if (showRawSignals) {
    return <RawSignals onBack={() => setShowRawSignals(false)} />;
  }

  return (
    <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Incident Management System</h1>
        <button
          onClick={() => setShowRawSignals(true)}
          style={{ padding: '8px 16px', background: '#1976d2', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
        >
          📊 View Raw Signals
        </button>
      </div>

      <div style={{ display: 'flex', gap: '40px' }}>
        {/* Left Side: Table */}
        <div style={{ flex: 1 }}>
          <h2>Incidents Dashboard</h2>
          <table border="1" cellPadding="10" style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ background: '#f4f4f4' }}>
                <th>ID</th>
                <th>Component</th>
                <th>Status</th>
                <th>MTTR</th>
              </tr>
            </thead>
            <tbody>
              {incidents.length === 0 ? (
                <tr><td colSpan="4">No incidents found</td></tr>
              ) : (
                incidents.map((inc) => (
                  <tr
                    key={inc.id}
                    onClick={() => { setSelectedIncident(inc); setRcaText(inc.rca || ''); }}
                    style={{ cursor: 'pointer', background: selectedIncident?.id === inc.id ? '#e0f7fa' : 'white' }}
                  >
                    <td>{inc.id}</td>
                    <td>{inc.component}</td>
                    <td>{inc.status}</td>
                    <td>{inc.mttr || '-'}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Right Side: Details */}
        <div style={{ flex: 1, border: '1px solid #ccc', padding: '20px', borderRadius: '5px' }}>
          {selectedIncident ? (
            <div>
              <h2>Incident Details: #{selectedIncident.id}</h2>
              <p><strong>Component:</strong> {selectedIncident.component}</p>
              <p><strong>Status:</strong> {selectedIncident.status}</p>
              <p><strong>Current RCA:</strong> {selectedIncident.rca || 'None'}</p>

              <div style={{ margin: '20px 0' }}>
                <button onClick={() => updateStatus(selectedIncident.id, 'INVESTIGATING')}>Set INVESTIGATING</button>
                <button onClick={() => updateStatus(selectedIncident.id, 'RESOLVED')} style={{ margin: '0 10px' }}>Set RESOLVED</button>
                <button onClick={() => updateStatus(selectedIncident.id, 'CLOSED')}>Set CLOSED</button>
              </div>

              <div>
                <h3>Root Cause Analysis (RCA)</h3>
                <textarea
                  value={rcaText}
                  onChange={(e) => setRcaText(e.target.value)}
                  rows="4"
                  style={{ width: '100%', marginBottom: '10px' }}
                  placeholder="Enter RCA here..."
                />
                <br />
                <button onClick={() => submitRca(selectedIncident.id)}>Submit RCA</button>
                <button
                  onClick={suggestRca}
                  disabled={loadingAi}
                  style={{ marginLeft: '10px', background: '#d1c4e9', border: '1px solid #9575cd', cursor: 'pointer' }}
                >
                  {loadingAi ? 'Thinking...' : '✨ Suggest RCA (AI)'}
                </button>
              </div>
            </div>
          ) : (
            <p>Click on an incident to view details.</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
