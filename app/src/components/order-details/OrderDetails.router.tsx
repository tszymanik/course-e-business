import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import OrderDetails from './OrderDetails';
import AddOrderDetail from './add/AddOrderDetail'

const OrderDetailsRouter = () => {
  return (
    <Switch>
      <Route exact path="/order-details" component={OrderDetails} />
      <Route path="/order-details/add" component={AddOrderDetail} />
    </Switch>
  );
}

export default OrderDetailsRouter;
