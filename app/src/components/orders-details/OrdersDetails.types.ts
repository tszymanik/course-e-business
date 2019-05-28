export type OrderDetails = {
  id: number;
  productId: number;
  unitPrice: number;
  quantity: number;
};

export type OrderDetailsPostData = {
  productId: number;
  unitPrice: number;
  quantity: number;
};