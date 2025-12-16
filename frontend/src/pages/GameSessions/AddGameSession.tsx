import { useContext, useState } from 'react';
import type { BoardGame } from '../../types/boardgame';
import { AuthContext } from '../../context/AuthContext';

export default function AddGameSession() {
    
    const auth = useContext(AuthContext);

    const [formData, setFormData] = useState({
        date: '',
        playercount: 1,
        description: '',
        owner: auth?.username || '',
        boardgame: {} as BoardGame,
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'playercount' ? parseInt(value) : value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await fetch('http://localhost:8080/sessions/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                throw new Error('Failed to add game session');
            }

            setFormData({
                date: '',
                playercount: 1,
                description: '',
                owner: auth?.username || '',
                boardgame: {} as BoardGame,
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
                    name="playercount"
                    min="1"
                    value={formData.playercount}
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