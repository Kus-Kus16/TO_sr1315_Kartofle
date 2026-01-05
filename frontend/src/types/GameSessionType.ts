import type { BoardGameTypeFull } from "./BoardGameType.ts";
import type {UserType} from "./UserType.ts";
import type {VoteTypeFull} from "./VoteType.ts";

interface GameSessionTypeBasic {
    title: string;
    date: string;
    numberOfPlayers: number;
    description: string;
}

export interface GameSessionTypeCreate extends GameSessionTypeBasic {
    boardGamesIds: number[];
}

export interface GameSessionTypePreview extends GameSessionTypeBasic {
    id: number;
    ownerId: number;
    participants: UserType[];
    selectedBoardGame: BoardGameTypeFull | undefined;
}

export interface GameSessionTypeFull extends GameSessionTypeBasic {
    id: number;
    ownerId: number;
    participants: UserType[];
    selectedBoardGameId?: number;
    boardGames: BoardGameTypeFull[];
    votes: VoteTypeFull[];
}