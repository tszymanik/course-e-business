export type Order = {
  id: number;
  userId: number;
  orderDate: Date;
  shippedDate: Date;
};

export type OrderPostData = {
  userId: number;
  orderDate: string;
  shippedDate: string;
};