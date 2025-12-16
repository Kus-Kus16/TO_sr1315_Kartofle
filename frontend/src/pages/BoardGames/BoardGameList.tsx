import { useEffect, useState } from "react";
import BoardGamePreview from "./BoardGamePreview";
import type { BoardGame } from "../../types/boardgame";
import AddBoardGame from "./AddBoardGame";


export default BoardGameList;function BoardGameList() {
  const [boardGames, setBoardGames] = useState<BoardGame[]>([]);

  useEffect(() => {
    fetch('http://localhost:8080/boardgames')
      .then(res => res.json())
      .then(data => {
        setBoardGames(data);        
      })
      .catch(err => console.error(err));
  }, []);

  useEffect(() => {
    console.log(boardGames);
  }, [boardGames])

  const handleAddGame = (newGame: BoardGame) => {
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
