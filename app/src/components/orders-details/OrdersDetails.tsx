import axios from 'axios';
import React, { Component } from 'react';

import { OrderDetails } from './OrdersDetails.types';

type OrdersState = {
  ordersDetails: OrderDetails[];
};

class OrdersDetails extends Component<any, OrdersState> {
  state: OrdersState = {
    ordersDetails: [],
  };

  componentDidMount() {
    this.fetch();
  }

  render() {
    const { ordersDetails } = this.state;
    return (
      <div className="container">
        <h1>Orders details</h1>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Product id</th>
              <th scope="col">Unit price</th>
              <th scope="col">Quantity</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            {ordersDetails.map((orderDetails) => {
              return (
                <tr key={orderDetails.id}>
                  <td>{orderDetails.id}</td>
                  <td>{orderDetails.productId}</td>
                  <td>{orderDetails.unitPrice}</td>
                  <td>{orderDetails.quantity}</td>
                  <td>
                    <button
                      className="btn btn-secondary"
                      onClick={async () => {
                        try {
                          await axios.delete(`/orders-details/${orderDetails.id}`);
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
      const ordersDetailsResponse = await axios.get('/orders-details');
      const ordersDetails: OrderDetails[] = ordersDetailsResponse.data;

      this.setState({
        ordersDetails,
      });
    } catch (error) {
      console.log(error);
    }
  }
}

export default OrdersDetails;
