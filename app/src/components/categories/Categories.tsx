import axios from 'axios';
import React, { Component } from 'react';

import { Category } from './Categories.types';

type CategoriesState = {
  categories: Category[];
};

class Categories extends Component<any, CategoriesState> {
  state: CategoriesState = {
    categories: [],
  };

  componentDidMount() {
    this.fetch();
  }

  render() {
    const { categories } = this.state;
    return (
      <div className="container">
        <h1>Categories</h1>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Name</th>
              <th scope="col">Description</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            {categories.map((category) => {
              return (
                <tr key={category.id}>
                  <td>{category.id}</td>
                  <td>{category.name}</td>
                  <td>{category.description}</td>
                  <td>
                    <button
                      className="btn btn-secondary"
                      onClick={async () => {
                        try {
                          await axios.delete(`/categories/${category.id}`);
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
      const categoriesResponse = await axios.get('/categories');
      const categories: Category[] = categoriesResponse.data;

      this.setState({
        categories,
      });
    } catch (error) {
      console.log(error);
    }
  }
}

export default Categories;
