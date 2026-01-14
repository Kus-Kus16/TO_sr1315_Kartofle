export interface VoteTypeCreate {
    boardGameId: number;
    likes: boolean;
    knows: boolean;
}

export interface VoteTypeDetails extends VoteTypeCreate {
    userId: number;
}