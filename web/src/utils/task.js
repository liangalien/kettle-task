const task = {};

task.statusList = [
    {value: 0, label: '未开始', color: '#6c757d'},
    {value: 1, label: '等待中', color: '#007bff'},
    {value: 2, label: '已就绪', color: '#007bff'},
    {value: 3, label: '执行中', color: '#e6a23c'},
    {value: 4, label: '成功', color: '#28a745'},
    {value: 5, label: '失败', color: '#dc3545'},
    {value: 6, label: '停止', color: '#dc3545'},
    {value: 7, label: '错误', color: '#dc3545'},
    {value: 99, label: '其它', color: '#6c757d'},
];

let statusMap = {};
task.statusList.map(s => {
    statusMap[s.value] = s;
})
task.statusMap = statusMap;


export default task;
