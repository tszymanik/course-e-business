import axios from 'axios';
import React, { Component } from 'react';

import { UserPostData } from '../Users.types';

type AddUserState = {
  email: string;
  password: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  phone: string;
};

class AddUser extends Component<any, AddUserState> {
  state: AddUserState = {
    email: '',
    password: '',
    address: '',
    city: '',
    postalCode: '',
    country: '',
    phone: '',
  };

  render() {
    const {
      email,
      password,
      address,
      city,
      postalCode,
      country,
      phone,
    } = this.state;

    return (
      <div className="container">
        <h1>Add user</h1>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            try {
              if (
                email !== null
                && password !== null
                && address !== null
                && city !== null
                && postalCode !== null
                && country !== null
                && phone !== null
              ) {
                const data: UserPostData = {
                  email,
                  password,
                  address,
                  city,
                  postalCode,
                  country,
                  phone,
                };

                await axios.post(
                  '/users',
                  data,
                );

                this.setState({
                  email: '',
                  password: '',
                  address: '',
                  city: '',
                  postalCode: '',
                  country: '',
                  phone: '',
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
              value={email}
              onChange={(e) => {
                this.setState({ email: e.target.value });
              }}
              className="form-control"
              placeholder="Email"
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              value={password}
              onChange={(e) => {
                this.setState({ password: e.target.value });
              }}
              className="form-control"
              placeholder="Password"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={address}
              onChange={(e) => {
                this.setState({ address: e.target.value });
              }}
              className="form-control"
              placeholder="Address"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={city}
              onChange={(e) => {
                this.setState({ city: e.target.value });
              }}
              className="form-control"
              placeholder="City"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={postalCode}
              onChange={(e) => {
                this.setState({ postalCode: e.target.value });
              }}
              className="form-control"
              placeholder="Postal code"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={country}
              onChange={(e) => {
                this.setState({ country: e.target.value });
              }}
              className="form-control"
              placeholder="Country"
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              value={phone}
              onChange={(e) => {
                this.setState({ phone: e.target.value });
              }}
              className="form-control"
              placeholder="Phone"
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

export default AddUser;
