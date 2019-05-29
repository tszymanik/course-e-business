import React from 'react';
import {
  BrowserRouter,
  Switch,
  Route,
} from 'react-router-dom';
import axios from 'axios';

import ProductsRouter from './components/products/Products.router';
import CategoriesRouter from './components/categories/Categories.router';
import UserRouter from './components/users/Users.router';
import OrdersRouter from './components/orders/Orders.router';
import OrderDetailsRouter from './components/order-details/OrderDetails.router';

axios.defaults.baseURL = 'http://localhost:9000';

const App = () => {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/products" component={ProductsRouter} />
        <Route path="/categories" component={CategoriesRouter} />
        <Route path="/users" component={UserRouter} />
        <Route path="/orders" component={OrdersRouter} />
        <Route path="/order-details" component={OrderDetailsRouter} />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
