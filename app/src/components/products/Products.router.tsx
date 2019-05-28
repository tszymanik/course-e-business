import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import Products from './Products';
import AddProduct from './add/AddProduct';

const ProductsRouter = () => {
  return (
    <Switch>
      <Route exact path="/products" component={Products} />
      <Route path="/products/add" component={AddProduct} />
    </Switch>
  );
}

export default ProductsRouter;
