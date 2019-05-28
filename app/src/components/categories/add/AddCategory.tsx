import axios from 'axios';
import React, { Component } from 'react';

import { CategoryPostData } from '../Categories.types';

type AddProductState = {
  name: string;
  description: string;
};

class AddProduct extends Component<any, AddProductState> {
  state: AddProductState = {
    name: '',
    description: '',
  };

  render() {
    const {
      name,
      description,
    } = this.state;

    return (
      <div className="container">
        <h1>Add category</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            try {
              if (
                name !== null
                && description !== null
              ) {
                const data: CategoryPostData = {
                  name,
                  description,
                };

                await axios.post(
                  '/categories',
                  data,
                );

                this.setState({
                  name: '',
                  description: '',
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
