import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../../context/AuthContext';

export default function AddGameSession({ boardGameId }: { boardGameId: number }) {
    
    const auth = useContext(AuthContext);
    const id = boardGameId;

    const [formData, setFormData] = useState({
        boardGameId: id,
        ownerUsername: auth?.username || '',
        date: '',
        numberOfPlayers: 1,
        description: '',
    });

    useEffect(() => {
        setFormData(prev => ({ ...prev, ownerUsername: auth?.username || '' }));
    }, [auth?.username]);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'numberOfPlayers' ? parseInt(value) : value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        if (!formData.date) {
            setError('Please select a date for the session');
            setLoading(false);
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/sessions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                const text = await response.text();
                throw new Error(text || `Failed to add game session (${response.status})`);
            }

            setFormData({
                boardGameId: id,
                ownerUsername: auth?.username || '',
                date: '',
                numberOfPlayers: 1,
                description: '',
            });
            alert('Game session added successfully!');
        } catch (err) {
            setError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="add-game-session">
            <h2>Add Game Session</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleSubmit}>
                <input
                    type="date"
                    name="date"
                    value={formData.date}
                    onChange={handleChange}
                    required
                />
                <br />
                <input
                    type="number"
                    name="numberOfPlayers"
                    min="1"
                    value={formData.numberOfPlayers}
                    onChange={handleChange}
                    required
                />
                <br />
                <textarea
                    name="description"
                    placeholder="Description"
                    value={formData.description}
                    onChange={handleChange}
                />
                <br />
                <button type="submit" disabled={loading}>
                    {loading ? 'Adding...' : 'Add Session'}
                </button>
            </form>
        </div>
    );
}