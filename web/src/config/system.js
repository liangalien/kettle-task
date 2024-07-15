import Task from "@pages/task/index";
import Runner from "@pages/runner/index";
import Repo from "@pages/repo/index";
import Project from "@pages/project/index";
import Cron from "@pages/cron/index";

const System = {};

System.routes = [
  {
    path: "/", name: "项目管理", component: Project, hide: true
  },
  {
    path: "/project", name: "项目管理", component: Project
  },
  {
    path: "/repo", name: "资源管理", component: Repo
  },
  {
    path: "/task", name: "任务集成", component: Task,
    children: [
      { path: "/task/list", name: "任务列表", component: Task }
    ]
  },
  {
    path: "/runner", name: "任务记录", component: Runner
  },
  {
    path: "/cron", name: "定时管理", component: Cron
  }
];

System.routes.reverse();

export default System;
