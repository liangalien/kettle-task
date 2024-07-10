import Task from '@pages/task/index';
import Runner from '@pages/runner/index';
import Repo from '@pages/repo/index';

const System = {};

System.routes = [
    /*{
        path: '/project', name: '项目管理', component: Task,
    },*/
    {
        path: '/repo', name: '资源管理', component: Repo,
    },
    {
        path: '/task', name: '任务集成', component: Task,
        children: [
            {path: '/task/list', name: '任务列表', component: Task}
        ]
    },
    {
        path: '/runner', name: '任务记录', component: Runner,
    },
]

export default System;
