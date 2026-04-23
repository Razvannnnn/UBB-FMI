import axios from 'axios';

const API_URL = 'http://localhost:8080/problema8/events';

export const getEvents = () => axios.get(API_URL);
export const getEventById = (id) => axios.get(`${API_URL}/${id}`);
export const createEvent = (event) => axios.post(API_URL, event);
export const updateEvent = (event) => axios.put(`${API_URL}/${event.id}`, event);
export const deleteEvent = (id) => axios.delete(`${API_URL}/${id}`);
