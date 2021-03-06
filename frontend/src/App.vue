<template>
  <div id="app">
    <div class="sticky-top">
      <NavBar class="navbar-first"></NavBar>
    </div>
    <div class="sticky-top-second">
      <NavBarSecond
        v-if="!isMobile"
        id="navbar-second"
        class="navbar-second"
      ></NavBarSecond>
    </div>

    <div class="sticky-bottom">
      <NavBarSecondMobile
        v-if="isMobile"
        id="navbar-second-mobile"
        class="navbar-second-mobile"
      ></NavBarSecondMobile>
    </div>
    <router-view class="main-view" />
  </div>
</template>

<script>
// @ is an alias to /src
import NavBar from "@/components/NavBars/NavBar.vue";
import NavBarSecond from "@/components/NavBars/NavBarSecond.vue";
import NavBarSecondMobile from "@/components/NavBars/NavBarSecondMobile.vue";

import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

import { mapGetters } from "vuex";

export default {
  name: "App",
  components: {
    NavBar,
    NavBarSecond,
    NavBarSecondMobile,
  },

  data() {
    return {
      isLogin: false,
      isMobile: false,
      currentScrollPos: 0,
      prevScrollpos: 0,
      user: {
        email: "",
        password: "",
      },
    };
  },
  created() {
    let item = sessionStorage.getItem("loginInfo");

    if (item != null) {
      axios.defaults.headers.common["access-token"] = JSON.parse(item)[
        "access-token"
      ];
      axios
        .get(`${SERVER_URL}/user/info`)
        .then(() => {
          //토큰이 유효하다면.
          this.$store.commit(
            "LOGIN",
            JSON.parse(sessionStorage.getItem("loginInfo"))
          ); //user없음.
        })
        .catch(() => {
          sessionStorage.removeItem("loginInfo");
          this.$store.dispatch("LOGOUT").then(() => this.$router.replace("/"));
        });
    }
  },
  computed: {
    ...mapGetters(["getAccessToken", "getUserEmail"]),
  },
  methods: {
    onClickLogout() {
      this.$store
        .dispatch("LOGOUT")
        .then(() => this.$router.replace("/").catch(() => {}));
    },
    // 화면 너비에 따른 모바일 여부 판단
    handleResize: function() {
      this.isMobile = window.innerWidth <= 480;
    },
    // 스크롤 방향 판단
    // 스크롤 올릴 때 NavBarSecondary 보여짐
    handleScroll: function() {
      this.currentScrollPos = window.pageYOffset;
      if (!this.isMobile) {
        if (
          document.location.href.includes("challenge-more-info") || //챌린지 상세 페이지
          this.prevScrollpos >= this.currentScrollPos
        ) {
          //내릴 때
          document.getElementById("navbar-second").style.top = "80px";
        } else {
          document.getElementById("navbar-second").style.top = "30px";
        }
      }
      this.prevScrollpos = this.currentScrollPos;
    },
  },
  mounted() {
    // 화면 너비 측정 이벤트 추가/
    this.handleResize();
    window.addEventListener("resize", this.handleResize);
    window.addEventListener("scroll", this.handleScroll);
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap");
#app {
  font-family: "Nanum Gothic", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

/* 상단고정 */
.sticky-top {
  position: sticky;
  top: 0;
  width: 100%;
  z-index: 20;
}

.sticky-top-second{
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 15;
}

.sticky-bottom {
  position: fixed;
  bottom: 0;
  width: 100%;
  z-index: 10;
}

/* NavBar */
.navbar-first {
  position: relative;
  z-index: 2;
}

/* NavbarSecondary*/
.navbar-second {
  position: relative;
  top:80px;
  transition: top 0.3s;
  height:50px;
}

.main-view {
  z-index: 1;
}

*::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}
*::-webkit-scrollbar-thumb {
  /* background-color: #e65c2e30; */
  background-color: #EAC03A30;
  border-radius: 10px;
  background-clip: padding-box;
  border: 1px solid transparent;
}
*::-webkit-scrollbar-track {
  background-color: rgba(255, 255, 255);
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}




/* mobile */
@media (max-width: 480px) {
  .main-view {
    margin-bottom: 50px !important;
  }
}

/* pc */
@media (min-width: 480px) {
.main-view {
    margin-top: 80px !important;
  }
}
</style>
