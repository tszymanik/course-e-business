import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import OrdersDetails from './OrdersDetails';
import AddOrderDetails from './add/AddOrderDetails'

const OrdersDetailsRouter = () => {
  return (
    <Switch>
      <Route exact path="/orders-details" component={OrdersDetails} />
      <Route path="/orders-details/add" component={AddOrderDetails} />
    </Switch>
  );
}

export default OrdersDetailsRouter;
