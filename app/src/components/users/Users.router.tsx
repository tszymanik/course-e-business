import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import Users from './Users';
import AddUser from './add/AddUser';

const ProductsRouter = () => {
  return (
    <Switch>
      <Route exact path="/users" component={Users} />
      <Route path="/users/add" component={AddUser} />
    </Switch>
  );
}

export default ProductsRouter;
