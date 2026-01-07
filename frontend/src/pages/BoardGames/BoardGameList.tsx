import { useEffect, useState } from "react";
import BoardGamePreview from "./BoardGamePreview";
import type { BoardGameType } from "../../types/boardgame";
import AddBoardGame from "./AddBoardGame";


export default BoardGameList;function BoardGameList() {
  const [boardGames, setBoardGames] = useState<BoardGameType[]>([]);

  useEffect(() => {
    fetch('http://localhost:8080/boardgames')
      .then(res => res.json())
      .then(data => {
        setBoardGames(data);        
      })
      .catch(err => console.error(err));
  }, []);

  const handleAddGame = (newGame: BoardGameType) => {
    setBoardGames(prev => [...prev, newGame]);
  };

  return (
    <div>
      <AddBoardGame onAddGame={handleAddGame} />
      <hr />
      {boardGames.map((game) => (
        <BoardGamePreview 
          key={game.id}
          boardGame={game}
        />
      ))}
    </div>
  )
}
