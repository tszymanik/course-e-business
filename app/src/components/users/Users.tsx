import axios from 'axios';
import React, { Component } from 'react';

import { User } from './Users.types';

type UsersState = {
  users: User[];
};

class Users extends Component<any, UsersState> {
  state: UsersState = {
    users: [],
  };

  componentDidMount() {
    this.fetch();
  }

  render() {
    const { users } = this.state;
    return (
      <div className="container">
        <h1>Users</h1>
        <table className="table table-bordered">
          <thead>
            <tr>
              <th scope="col">Id</th>
              <th scope="col">Email</th>
              <th scope="col">Address</th>
              <th scope="col">City</th>
              <th scope="col">Postal code</th>
              <th scope="col">Country</th>
              <th scope="col">Phone</th>
              <th scope="col"></th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => {
              return (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.email}</td>
                  <td>{user.address}</td>
                  <td>{user.city}</td>
                  <td>{user.postalCode}</td>
                  <td>{user.country}</td>
                  <td>{user.phone}</td>
                  <td>
                    <button
                      className="btn btn-secondary"
                      onClick={async () => {
                        try {
                          await axios.delete(`/users/${user.id}`);
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
      const usersResponse = await axios.get('/users');
      const users: User[] = usersResponse.data;

      this.setState({
        users,
      });
    } catch (error) {
      console.log(error);
    }
  }
}

export default Users;
