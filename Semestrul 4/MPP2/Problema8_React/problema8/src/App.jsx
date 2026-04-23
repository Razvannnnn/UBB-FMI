import { useEffect, useState } from 'react';
import { getEvents, createEvent, updateEvent, deleteEvent } from './api';
import EventForm from './components/EventForm';
import EventList from './components/EventList';

export default function App() {
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);

  const loadEvents = () => {
    getEvents().then(response => setEvents(response.data));
  };

  useEffect(() => {
    loadEvents();
  }, []);

  const handleSave = (event) => {
    const action = event.id ? updateEvent : createEvent;
    action(event).then(() => {
      loadEvents();
      setSelectedEvent(null);
    });
  };

  const handleEdit = (event) => {
    setSelectedEvent(event);
  };

  const handleDelete = (id) => {
    deleteEvent(id).then(() => loadEvents());
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
      <div>
        <h1>Gestionare Evenimente</h1>
        <EventForm onSave={handleSave} selectedEvent={selectedEvent} />
        <EventList events={events} onEdit={handleEdit} onDelete={handleDelete} />
      </div>
    </div>
  );
}
