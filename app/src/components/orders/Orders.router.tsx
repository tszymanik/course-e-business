import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import Orders from './Orders';
import AddOrder from './add/AddOrder';

const OrdersRouter = () => {
  return (
    <Switch>
      <Route exact path="/orders" component={Orders} />
      <Route path="/orders/add" component={AddOrder} />
    </Switch>
  );
}

export default OrdersRouter;
