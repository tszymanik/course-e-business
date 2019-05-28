import axios from 'axios';
import React, { Component } from 'react';

import { OrderDetailsPostData } from '../OrdersDetails.types';

type AddOrderState = {
  productId: string;
  unitPrice: string;
  quantity: string;
};

class AddOrderDetails extends Component<any, AddOrderState> {
  state: AddOrderState = {
    productId: '',
    unitPrice: '',
    quantity: '',
  };

  render() {
    const {
      productId,
      unitPrice,
      quantity,
    } = this.state;

    return (
      <div className="container">
        <h1>Add order details</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            try {
              if (
                productId !== null
                && unitPrice !== null
                && quantity !== null
              ) {
                const data: OrderDetailsPostData = {
                  productId: parseInt(productId),
                  unitPrice: parseInt(unitPrice),
                  quantity: parseInt(quantity),
                };

                await axios.post(
                  '/orders-details',
                  data,
                );

                this.setState({
                  productId: '',
                  unitPrice: '',
                  quantity: '',
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
              value={productId}
              onChange={(e) => {
                this.setState({ productId: e.target.value });
              }}
              className="form-control"
              placeholder="Product id"
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
              value={quantity}
              onChange={(e) => {
                this.setState({ quantity: e.target.value });
              }}
              className="form-control"
              placeholder="Quantity"
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

export default AddOrderDetails;
