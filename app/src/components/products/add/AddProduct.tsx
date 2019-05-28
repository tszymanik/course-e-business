import axios from 'axios';
import React, { Component } from 'react';

import { Category } from '../../categories/Categories.types';
import { ProductPostData } from '../Products.types';

type AddProductState = {
  name: string;
  description: string;
  categoryId: string;
  quantityPerUnit: string;
  unitPrice: string;
  unitsInStock: string;
  categories: Category[];
};

class AddProduct extends Component<any, AddProductState> {
  state: AddProductState = {
    name: '',
    description: '',
    categoryId: '',
    quantityPerUnit: '',
    unitPrice: '',
    unitsInStock: '',
    categories: [],
  };

  async componentDidMount() {
    const categoriesResponse = await axios.get('/categories');
    const categories = categoriesResponse.data;
    this.setState({ categories })
  }

  render() {
    const {
      name,
      description,
      categoryId,
      quantityPerUnit,
      unitPrice,
      unitsInStock,
      categories,
    } = this.state;

    return (
      <div className="container">
        <h1>Add product</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            try {
              if (
                name !== null
                && categoryId !== null
                && quantityPerUnit !== null
                && unitPrice !== null
                && unitsInStock !== null
              ) {
                const data: ProductPostData = {
                  name,
                  description,
                  categoryId: parseInt(categoryId),
                  quantityPerUnit,
                  unitPrice: parseInt(unitPrice),
                  unitsInStock: parseInt(unitsInStock),
                };

                await axios.post(
                  '/products',
                  data,
                );

                this.setState({
                  name: '',
                  description: '',
                  categoryId: '',
                  quantityPerUnit: '',
                  unitPrice: '',
                  unitsInStock: '',
                });
              }
            } catch (error) {
              console.log(error);
            }
          }}
        >
          <div className="form-group">
            <input
              type="text"
              value={name}
              onChange={(e) => {
                this.setState({ name: e.target.value });
              }}
              className="form-control"
              placeholder="Name"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={description}
              onChange={(e) => {
                this.setState({ description: e.target.value });
              }}
              className="form-control"
              placeholder="Description"
            />
          </div>
          <div className="form-group">
            <select
              className="form-control"
              value={categoryId}
              onChange={(e) => {
                this.setState({ categoryId: e.target.value });
              }}
            >
              <option selected>Category</option>
              {categories.map((category) => {
                return (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                )
              })}
            </select>
          </div>
          <div className="form-group">
            <input
              type="text"
              value={quantityPerUnit}
              onChange={(e) => {
                this.setState({ quantityPerUnit: e.target.value });
              }}
              className="form-control"
              placeholder="Quantity"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={unitPrice}
              onChange={(e) => {
                this.setState({ unitPrice: e.target.value });
              }}
              className="form-control"
              placeholder="Unit price"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={unitsInStock}
              onChange={(e) => {
                this.setState({ unitsInStock: e.target.value });
              }}
              className="form-control"
              placeholder="Units in stock"
            />
          </div>
          <button
            type="submit"
            className="btn btn-primary"
          >
            Send
            </button>
        </form>
      </div>
    );
  }
}

export default AddProduct;
