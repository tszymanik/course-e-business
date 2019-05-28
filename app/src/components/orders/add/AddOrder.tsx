import axios from 'axios';
import moment from 'moment';
import React, { Component } from 'react';

import { User } from '../../users/Users.types';
import { OrderPostData } from '../Orders.types';

type AddOrderState = {
  userId: string;
  orderDate: Date;
  shippedDate: Date;
  users: User[];
};

class AddOrder extends Component<any, AddOrderState> {
  state: AddOrderState = {
    userId: '',
    orderDate: new Date(),
    shippedDate: new Date(),
    users: [],
  };

  async componentDidMount() {
    const usersResponse = await axios.get('/users');
    const users: User[] = usersResponse.data;

    this.setState({ users });
  }

  render() {
    const {
      userId,
      orderDate,
      shippedDate,
      users,
    } = this.state;

    return (
      <div className="container">
        <h1>Add order</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            try {
              if (
                userId !== null
                && orderDate !== null
                && shippedDate !== null
              ) {
                const data: OrderPostData = {
                  userId: parseInt(userId),
                  orderDate: moment(orderDate).format('YYYY-MM-DD HH:mm:ss'),
                  shippedDate: moment(shippedDate).format('YYYY-MM-DD HH:mm:ss'),
                };

                await axios.post(
                  '/orders',
                  data,
                );

                this.setState({
                  userId: '',
                  orderDate: new Date(),
                  shippedDate: new Date(),
                });
              }
            } catch (error) {
              console.log(error);
            }
          }}
        >
          <div className="form-group">
            <select
              className="form-control"
              value={userId}
              onChange={(e) => {
                this.setState({ userId: e.target.value });
              }}
            >
              <option value="">User id</option>
              {users.map((user) => {
                return (
                  <option key={user.id} value={user.id}>
                    {user.email}
                  </option>
                )
              })}
            </select>
          </div>
          <div className="form-group">
            <input
              type="date"
              value={moment(orderDate).format('YYYY-MM-DD')}
              onChange={(e) => {
                this.setState({ orderDate: moment(e.target.value).toDate() });
              }}
              className="form-control"
              placeholder="Order date"
            />
          </div>
          <div className="form-group">
            <input
              type="date"
              value={moment(shippedDate).format('YYYY-MM-DD')}
              onChange={(e) => {
                this.setState({ shippedDate: moment(e.target.value).toDate() });
              }}
              className="form-control"
              placeholder="Shipped date"
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

export default AddOrder;
