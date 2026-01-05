export interface VoteTypeCreate {
    boardGameId: number;
    likes: boolean;
    knows: boolean;
}

export interface VoteTypeFull extends VoteTypeCreate {
    userId: number;
}