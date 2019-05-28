import React from 'react';
import {
  BrowserRouter,
  Switch,
  Route,
} from 'react-router-dom';
import axios from 'axios';

import Home from './components/home/Home';
import ProductsRouter from './components/products/Products.router';
import CategoriesRouter from './components/categories/Categories.router';
import UserRouter from './components/users/Users.router';
import OrdersRouter from './components/orders/Orders.router';
import OrdersDetailsRouter from './components/orders-details/OrdersDetails.router';
import './App.scss';

axios.defaults.baseURL = 'http://localhost:9000';

const App = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route path="/products" component={ProductsRouter} />
        <Route path="/categories" component={CategoriesRouter} />
        <Route path="/users" component={UserRouter} />
        <Route path="/orders" component={OrdersRouter} />
        <Route path="/orders-details" component={OrdersDetailsRouter} />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
