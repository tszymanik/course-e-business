import React from 'react';
import {
  Switch,
  Route,
} from 'react-router-dom';

import Categories from './Categories';
import AddCategory from './add/AddCategory';

const ProductsRouter = () => {
  return (
    <Switch>
      <Route exact path="/categories" component={Categories} />
      <Route path="/categories/add" component={AddCategory} />
    </Switch>
  );
}

export default ProductsRouter;
