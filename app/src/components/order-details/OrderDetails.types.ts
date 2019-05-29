export type OrderDetails = {
  id: number;
  productId: number;
  unitPrice: number;
  quantity: number;
};

export type OrderDetailPostData = {
  productId: number;
  unitPrice: number;
  quantity: number;
};
