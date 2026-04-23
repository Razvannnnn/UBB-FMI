import { useState, useEffect } from 'react';

function EventForm({ onSave, selectedEvent }) {
  const [event, setEvent] = useState({ name: '', distance: '', ageGroupId: '' });

  useEffect(() => {
    if (selectedEvent) {
      setEvent(selectedEvent);
    }
  }, [selectedEvent]);

  const handleChange = (e) => {
    setEvent({ ...event, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(event);
    setEvent({ name: '', distance: '', ageGroupId: '' });
  };

  return (
    <div >
      <form onSubmit={handleSubmit} >
      <h2>{selectedEvent ? 'Editeaza Eveniment' : 'Adauga Eveniment'}</h2>
      <input name="name" placeholder="Nume" value={event.name} onChange={handleChange} required />
      <input name="distance" placeholder="Distanta" value={event.distance} onChange={handleChange} required />
      <input name="ageGroupId" placeholder="Age Group ID" value={event.ageGroupId} onChange={handleChange} required />
      <button type="submit">{selectedEvent ? 'Actualizeaza' : 'Salveaza'}</button>
    </form>
    </div>
  );
}

export default EventForm;