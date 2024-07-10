import Scrollbar from 'element-ui/packages/scrollbar/src/main';

export default {
  extends: Scrollbar,

  methods: {
    handleScroll() {
      const wrap = this.wrap;

      this.moveY = ((wrap.scrollTop * 100) / wrap.clientHeight);
      this.moveX = ((wrap.scrollLeft * 100) / wrap.clientWidth);

      const bottom = wrap.scrollHeight - wrap.scrollTop.toFixed(0) <= wrap.clientHeight; // 是否滑动到底部
      if (bottom) {
        this.$emit('scroll-bottom');
      }

      if (wrap.scrollTop.toFixed(0)) {
        this.$emit('scrollTop');
      }
    },
    scrollTop() {
      const wrap = this.wrap;
      wrap.scrollTop = 0;
    },
    scrollBottom() {
      const wrap = this.wrap;
      wrap.scrollTop = 9999999;
    }
  }
};
