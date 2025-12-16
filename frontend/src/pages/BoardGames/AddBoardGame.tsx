import React from 'react'
import { useState } from 'react'
import type { BoardGame } from '../../types/boardgame';

type AddBoardGameProps = {
    onAddGame: (newGame: BoardGame) => void;
}

export default function AddBoardGame({onAddGame}: AddBoardGameProps) {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        minPlayers: 1,
        maxPlayers: 1,
        minutesPlaytime: 10,
    })
    const [error, setError] = useState("")

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({
            ...prev,
            [name]: name === 'title' || name === 'description' ? value : parseInt(value),
        }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError('')

        try {
            const response = await fetch('http://localhost:8080/boardgames', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            })

            if (!response.ok) throw new Error('Failed to add board game')

            const newBoardGame = await response.json();
            onAddGame(newBoardGame);

            setFormData({ title: '', description: '', minPlayers: 1, maxPlayers: 1, minutesPlaytime: 10 })

            alert('Board game added successfully!')
        } catch (err) {
            setError('An error occurred');
            console.log(err);
        }
    }

    return (
        <form onSubmit={handleSubmit}>
            <label>
            Title:
            <input name="title" placeholder="Title" value={formData.title} onChange={handleChange} required />
            </label>
            <br/>
            <label>
            Description:
            <textarea name="description" placeholder="Description" value={formData.description} onChange={handleChange} />
            </label>
            <br/>
            <label>
            Min Players:
            <input name="minPlayers" type="number" placeholder="Min Players" value={formData.minPlayers} onChange={handleChange} />
            </label>
            <br/>
            <label>
            Max Players:
            <input name="maxPlayers" type="number" placeholder="Max Players" value={formData.maxPlayers} onChange={handleChange} />
            </label>
            <br/>
            <label>
            Minutes Playtime:
            <input name="minutesPlaytime" type="number" placeholder="Minutes Playtime" value={formData.minutesPlaytime} onChange={handleChange} />
            </label>
            <br/>
            <button type="submit">Add Board Game</button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </form>
    )
}
