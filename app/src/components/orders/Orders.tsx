import axios from 'axios';
import moment from 'moment';
import React, { Component } from 'react';

import { Order } from './Orders.types';

type OrdersState = {
  orders: Order[];
};

class Orders extends Component<any, OrdersState> {
  state: OrdersState = {
    orders: [],
  };

  componentDidMount() {
    this.fetch();
  }

  render() {
    const { orders } = this.state;
    return (
      <div className="container">
        <h1>Orders</h1>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">User id</th>
              <th scope="col">Order date</th>
              <th scope="col">Shipped date</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => {
              return (
                <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>{order.userId}</td>
                  <td>{moment(order.orderDate).format('YYYY-MM-DD')}</td>
                  <td>{moment(order.shippedDate).format('YYYY-MM-DD')}</td>
                  <td>
                    <button
                      className="btn btn-secondary"
                      onClick={async () => {
                        try {
                          await axios.delete(`/orders/${order.id}`);
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
      const ordersResponse = await axios.get('/orders');
      const orders: Order[] = ordersResponse.data;

      this.setState({
        orders,
      });
    } catch (error) {
      console.log(error);
    }
  }
}

export default Orders;
