function EventList({ events, onEdit, onDelete }) {
  return (
    <div>
      <h2>Lista Evenimentelor</h2>
      <ul>
        {events.map(event => (
          <li key={event.id}>
            {event.name} ({event.distance}km) - Age Group ID: {event.ageGroupId}
            <button onClick={() => onEdit(event)}>Edit</button>
            <button onClick={() => onDelete(event.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default EventList;