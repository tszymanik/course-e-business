export type Product = {
  id: number;
  name: string;
  categoryId: number;
  quantityPerUnit: string;
  unitPrice: number;
  unitsInStock: number;
};

export type ProductPostData = {
  name: string;
  description: string;
  categoryId: number;
  quantityPerUnit: string;
  unitPrice: number;
  unitsInStock: number;
};