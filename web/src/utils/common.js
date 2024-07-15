const Common = {};

Common.decodeURI = (str) => {
  return window.decodeURIComponent(str);
};
/**
 * 转换到UTF编码[@到%40]
 */
Common.encodeURI = (str) => {
  return window.encodeURIComponent(str);
};

Common.getUrlParams = (query) => {
  if (!query) {
    // query = location.search.substring(1);
    query = Common.getUrlInfo().query;
  } else {
    query = query.split('?');
    query = query[query.length - 1];
  }
  var pairs = query.split('&'), args = {};
  for (var i = 0; i < pairs.length; i++) {
    var pos = pairs[i].indexOf('=');
    if (pos == -1) {
      continue;
    }
    var argname = pairs[i].substring(0, pos), value = pairs[i].substring(pos + 1);
    value = Common.decodeURI(value);
    if (!args[argname]) {
      args[argname] = value;
    } else {
      if (args[argname] instanceof Array) {
        args[argname].push(value);
      } else {
        var val = args[argname];
        args[argname] = [];
        args[argname].push(val);
        args[argname].push(value);
      }
    }
  }
  return args;
};

Common.getUrlParam = (name) => {
  return Common.getUrlParams()[name];
};

Common.setUrlParam = (name, value) => {
  var params = Common.getUrlParams();
  params[name] = value;
  Common.setUrlParams(params);
};

Common.setUrlParams = (params) => {
  const { isHashRoute, query, search, hash } = Common.getUrlInfo();

  const urlQuery = Common.objToUrlQuery(params);

  let newSearch = '', newQuery = '';
  if (isHashRoute) {
    newSearch = search + hash;
    newQuery = urlQuery;
  } else {
    newSearch = '';
    newQuery = urlQuery + hash;
  }
  // const url = isHashRoute ? (search + hash + '?' + urlQuery) : ('?' + urlQuery + hash);
  history.pushState(null, null, newQuery ? newSearch + '?' + newQuery : null);
};

Common.getUrlInfo = () => {
  const { hash: initHash, search } = window.location;
  const isHashRoute = initHash.includes('#/');
  return {
    isHashRoute,
    query: isHashRoute ? (initHash.split('?')[1] || '') : search.substr(1),
    search,
    hash: initHash.split('?')[0]
  };
};

Common.getOrigin = () => {
  return window.location.origin;
};

Common.objToUrlQuery = (obj) => {
  if (!obj) return '';
  var pairs = [];
  for (var key in obj) {
    var value = obj[key];
    if (['', undefined, null].includes(value)) {
      continue;
    }
    if (value instanceof Array) {
      for (var i = 0; i < value.length; ++i) {
        pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(value[i]));
      }
      continue;
    }
    pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
  }
  if (pairs.length > 0) { return pairs.join('&'); } else { return ''; }
};

/**
 * 将日期格式转换成中文
 * @param dateString 日期格式文本
 * @param minDay 最小粒度是天
 * @returns {string}
 * @constructor
 */
Common.DateFormat = (dateString, minDay = false) => {
  if (!dateString) return '';

  var dateTimeStamp = Date.parse(dateString);

  var minute = 1000 * 60;
  var hour = minute * 60;
  var day = hour * 24;
  var month = day * 30;
  var year = month * 12;

  var now = new Date().getTime();
  var diffValue = now - dateTimeStamp;
  var postText = diffValue < 0 ? '后' : '前';
  diffValue = Math.abs(diffValue);

  var yearC = diffValue / year;
  var monthC = diffValue / month;
  var weekC = diffValue / (7 * day);
  var dayC = diffValue / day;
  var hourC = diffValue / hour;
  var minC = diffValue / minute;
  var result = null;
  if (yearC >= 1) {
    result = parseInt(yearC) + '年' + postText;
  } else if (monthC >= 1) {
    result = parseInt(monthC) + '个月' + postText;
  } else if (weekC >= 1) {
    result = parseInt(weekC) + '周' + postText;
  } else if (dayC >= 1) {
    result = parseInt(dayC) + '天' + postText;
  } else if (hourC >= 1) {
    result = minDay ? '今天' : parseInt(hourC) + '小时' + postText;
  } else if (minC >= 1) {
    result = minDay ? '今天' : parseInt(minC) + '分钟' + postText;
  } else { result = minDay ? '今天' : '刚刚'; }
  return result;
};

Common.getCurPath = () => {
  return window.location.pathname + window.location.search;
};

Common.getStorage = (name, defaultValue = null) => {
  return window.localStorage.getItem(name) || defaultValue;
};

Common.setStorage = (name, value) => {
  return window.localStorage.setItem(name, value);
};

Common.removeStorage = (name) => {
  return window.localStorage.removeItem(name);
};

Common.getProjectId = () => {
  return Common.getStorage('project_id');
};

Common.getProjectName = () => {
  return Common.getStorage('project_name');
};

Common.setProject = (id, name) => {
  Common.setStorage('project_id', id);
  Common.setStorage('project_name', name);
};

Common.getUserId = () => {
  return Common.getStorage('name');
};

Common.getUserName = () => {
  return Common.getStorage('nick_name');
};

Common.isBlank = (object) => {
  if (object === null || object === undefined) {
    return true;
  }

  if (object === "") {
    return true;
  }

  return false;
}

export default Common;
