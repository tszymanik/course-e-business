import axios from 'axios';
import React, { Component } from 'react';

import { Product } from './Products.types';
import { Category } from '../categories/Categories.types';

type ProductsState = {
  products: Product[];
  categories: Category[];
};

class Products extends Component<any, ProductsState> {
  state: ProductsState = {
    products: [],
    categories: [],
  };

  componentDidMount() {
    this.fetch();
  }

  render() {
    const { products, categories } = this.state;
    return (
      <div className="container">
        <h1>Products</h1>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Name</th>
              <th scope="col">Category</th>
              <th scope="col">Quantity</th>
              <th scope="col">UnitPrice</th>
              <th scope="col">UnitsInStock</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => {
              const category = categories.find((category) => category.id === product.categoryId)
              return (
                <tr key={product.id}>
                  <td>{product.id}</td>
                  <td>{product.name}</td>
                  <td>{category !== undefined && category.name}</td>
                  <td>{product.quantityPerUnit}</td>
                  <td>{product.unitPrice}</td>
                  <td>{product.unitsInStock}</td>
                  <td>
                    <button
                      className="btn btn-secondary"
                      onClick={async () => {
                        try {
                          await axios.delete(`/products/${product.id}`);
                          this.fetch();
                        } catch (error) {
                          console.log(error);
                        }
                      }}
                    >
                      Remove
                      </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    );
  }

  fetch = async () => {
    try {
      const productsResponse = await axios.get('/products');
      const products: Product[] = productsResponse.data;

      const categoriesResponse = await axios.get('/categories');
      const categories: Category[] = categoriesResponse.data;

      this.setState({
        products,
        categories,
      });
    } catch (error) {
      console.log(error);
    }
  }
}

export default Products;
