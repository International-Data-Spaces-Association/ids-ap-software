/* eslint-disable */
import Vue from 'vue';
import VueMeta from 'vue-meta';
import Router from 'vue-router';
import Main from '@/components/Main';

Vue.use(VueMeta);
Vue.use(Router);

const router = new Router({
  base: '/mqa/shacl-validator-ui',
  mode: 'history',
  linkActiveClass: 'active',
  routes: [
    {
      path: '',
      name: 'Main',
      component: Main
    },
  ]
});

router.beforeEach((to, from, next) => {
  next();
});

export default router;
