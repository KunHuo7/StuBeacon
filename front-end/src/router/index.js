import { createRouter, createWebHistory } from 'vue-router'
import {useUserStore} from "@/stores/userStore.js";
import {get} from "@/net/index.js"

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
     //AIIT欢迎你
    {
      name:'welcome',
      path:'/welcome',
      meta: {
        title: '欢迎来到AIIT'
      },
      component:()=>import('@/views/Auth/BackGround.vue'),
    },
      //登录
    {
      name:'登入',
      path:'/auth',
      meta:{
        title:'登录',
      },
      component:()=>import('@/views/Auth/Auth.vue')
    },
    {
      name:'framework',
      path:'/',
      component:()=>import('@/components/zuJian/Header.vue'),
      children: [
        {
        name:'index',
        path:'/index',
        meta: {
          title: '首页'
        },
        component:()=>import('@/views/Common/Index.vue')
        },
        {
          name:'main',
          path:'/',
          meta: {
            title: '首页'
          },
          component:()=>import('@/views/Common/MainBlog.vue'),

        },
        // AI聊天完整页面
        {
          name:'chatFullPage',
          path:'/chat',
          meta: {
            title: 'AI助手'
          },
          component:()=>import('@/views/chat/AIchat.vue'),
        },
        // 带ID的聊天页面，用于打开特定对话
        {
          name:'chatWithId',
          path:'/chat/:id',
          meta: {
            title: 'AI对话'
          },
          component:()=>import('@/views/chat/AIchat.vue'),
        },
          //公告
        {
          name: 'viewBlog',
          path: '/view/:id',
          meta: {
            title: '公告详情'
          },
          component: () => import('@/views/Common/ViewBlog.vue')
        },
          //话题讨论
        {
          name: 'talk',
          path: '/talk',
          meta: {
            title: '话题讨论'
          },
          component: () => import('@/views/dropdown/talk/Talk.vue')
        },
          //话题讨论
        {
          name: 'topicDetail',
          path: '/talk/detail/:id',
          meta: {
            title: '话题详情'
          },
          component: () => import('@/views/dropdown/talk/zuJian/TopicDetail.vue')
        },
          //话题讨论
        {
          name: 'createTopic',
          path: '/talk/create',
          meta: {
            title: '创建话题'
          },
          component: () => import('@/views/dropdown/talk/zuJian/CreateTopic.vue')
        },
          //个人中心
        {
          name:'personal',
          path:'/personal',
          meta: {
            title: '个人中心'
          },
          component:()=>import('@/views/column/Personal.vue'),
        },
          //课程规划
        {
          name:'courses',
          path:'/courses',
          meta: {
            title: '课程规划'
          },
          component:()=>import('@/views/dropdown/Other/courses.vue'),

        },
          //入学指南
        {
          name:'admission-guide',
          path:'/about/admission-guide',
          meta: {
            title: '入学指南'
          },
          component:()=>import('@/views/dropdown/about/admission-guide.vue'),

        },
          //校园概况
        {
          name:'school-map.vue',
          path:'/about/school-map',
          meta: {
            title: '校园地图'
          },
          component:()=>import('@/views/dropdown/about/school-map.vue'),

        },
          //学校简介
        {
          name:'school-profile',
          path:'/about/school-profile',
          meta: {
            title: '学校简介'
          },
          component:()=>import('@/views/dropdown/about/school-profile.vue'),
        },
          //学习指南
        {
          name:'study-guide',
          path:'/admission/study-guide',
          meta: {
            title: '学习指南'
          },
          component:()=>import('@/views/dropdown/admission/study-guide.vue'),
        },
          //奖励办法
        {
          name:'reward-policy',
          path:'/admission/reward-policy',
          meta: {
            title: '奖励办法'
          },
          component:()=>import('@/views/dropdown/admission/reward-policy.vue'),
        },
          //军训
        {
          name:'military-training',
          path:'/admission/military-training',
          meta: {
            title: '军训指南'
          },
          component:()=>import('@/views/dropdown/admission/military-training.vue'),
        },
          //营业厅
        {
          name:'service-hall',
          path:'/daily/service-hall',
          meta: {
            title: '营业厅'
          },
          component:()=>import('@/views/dropdown/daily/service-hall.vue'),
        },
          //医务室
        {
          name:'infirmary',
          path:'/daily/infirmary',
          meta: {
            title: '医务室'
          },
          component:()=>import('@/views/dropdown/daily/infirmary.vue'),
        },
          //校园食堂
        {
          name:'canteen',
          path:'/daily/canteen',
          meta: {
            title: '校园食堂'
          },
          component:()=>import('@/views/dropdown/daily/canteen.vue'),
        },
          //校园活动
        {
          name:'activity',
          path:'/activity',
          meta: {
            title: '校园活动'
          },
          component:()=>import('@/views/dropdown/activity/CampusActivity.vue'),
        },
          //校园活动
        {
          name:'activityDetail',
          path:'/activity/detail/:id',
          meta: {
            title: '活动详情'
          },
          component:()=>import('@/views/dropdown/activity/ActivityDetail.vue'),
        },
        {
          name:'manager',
          path:'/manager',
          meta: {
            title: '用户管理'
          },
          component:()=>import('@/views/dropdown/manage/Manager.vue'),
        },
        // {
        //   name:'not',
        //   path:'/not',
        //   component:()=>import('@/views/Common/Notification.vue'),
        // },
          //
        {
          name:'map',
          path:'/map',
          meta: {
            title: '校园导航 - 学子航标'
          },
          component:()=>import('@/components/zuJian/map/GaoDeMap.vue'),
        },
        {
          name:'new',
          path:'/new',
          meta: {
            title: '发布公告 - 学子航标'
          },
          component:()=>import('@/views/Common/EditBlog.vue'),
        },
        {
          path: '/edit-blog',
          name: 'EditBlog',
          component: () => import('@/views/Common/EditBlog.vue')
        },
      ]
    },
    {
      path: '/error/404',
      name: '404',
      component: () => import('@/views/error/404.vue'),
      meta: {
        title: '页面不存在'
      }
    },
    {
      path: '/error/401',
      name: '401',
      component: () => import('@/views/error/401.vue'),
      meta: {
        title: '未授权'
      }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/error/404'
    }
  ]
})


router.beforeEach((to,from,next)=>{
  const userStore = useUserStore()
  const publicMap = new Map()
  // publicMap.set('/welcome', 1)
  publicMap.set('/welcome',2)
  publicMap.set('/auth', 3)
  publicMap.set('/test', 4)
  publicMap.set('/error/401', 5)
  publicMap.set('/error/404', 6)
  // 检查要访问的路径是否是根路径
  if (to.matched.length === 0) next('error/404')
  if (to.meta.title) {
    document.title = to.meta.title
  } else {
    document.title = '学子航标'
  }
  if (!publicMap.has(to.path)) {
    // 不是访问根路径，检查用户状态
    const user = userStore.user; // 假设你的用户状态保存在Vuex的`user`状态中
    if (user === null) {
      get('api/user/myinfo', {},
          (message,data) => {
            userStore.login(data);
            next();
          }, () => {
            next('/welcome'); // 3秒后重定向到根路径
          }, () => {
            next('/welcome'); // 3秒后重定向到根路径
          }
      )
    } else {
      // 用户已登录，允许路由继续
      next();
    }
  } else {
    // 访问的是根路径，直接放行
    next();
  }

})

export default router
