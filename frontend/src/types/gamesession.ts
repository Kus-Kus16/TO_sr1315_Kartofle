import type { BoardGame } from "./boardgame";

export type GameSessionType = {
    id: number;
    date: string;
    playercount: number;
    description: string;
    owner: string;
    boardgame: BoardGame;
};