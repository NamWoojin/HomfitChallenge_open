<template>
  <div>
    <select-preference
      :modalShow="modalShow"
      :fit="fit"
  :body="body"
  :day="day"
      @modalClose="modalShow = false"
    ></select-preference>

    <div class="recommend-category-background row mx-auto mt-3">
      <div class="recommend-category mx-auto">
        <input
          class="recommend-checkbox-tools"
          type="radio"
          :value="1"
          v-model="recommendCategory"
          id="favorit-category"
          checked
        />
        <label for="favorit-category">
          선호 카테고리 추천
        </label>
        <input
          class="recommend-checkbox-tools"
          type="radio"
          :value="2"
          v-model="recommendCategory"
          id="favorit-bodypart"
        />
        <label for="favorit-bodypart"> 선호 운동 부위 추천 </label>
        <input
          class="recommend-checkbox-tools"
          type="radio"
          :value="3"
          v-model="recommendCategory"
          id="favorit-daylist"
        />
        <label for="favorit-daylist"> 선호 요일 추천 </label
        >
        <!-- <input
          class="recommend-checkbox-tools"
          type="radio"
          :value="4"
          v-model="recommendCategory"
          id="favorit-age-gender"
        />
        <label for="favorit-age-gender">
          나이 & 성별
        </label> -->
      </div>
    </div>
    <div class="col-12 col-md-8 mx-auto challenge-recommend-text">
      <span class="display-inline-block">
      <span class="challenge-recommend-highlight">{{ getUserNickName }}</span>
      <span>님이 선호하는 {{ getCategoryName }} </span>
      </span>
      <span class="ml-1 display-inline-block">
      <span class="challenge-recommend-highlight">{{getCategoryList}}</span>
      <span>입니다.</span>
      <b-button id="challenge-recommend-edit-button" v-b-tooltip.hover.bottom="'선호정보 수정'" @click="modalShow = true"><b-icon icon="pencil"></b-icon></b-button>
      </span>
    </div>
    <div class="row col-12 col-md-8 mx-auto recommend-list-card">
      <challenge-list-card
        v-for="(challenge, index) in challengeList"
        :key="`${index}_challenge`"
        class="col-6 col-md-4 col-xl-3 recommend-challenge-list-card"
        :challenge="challenge"
        @moreInfo="ChallengeMoreInfo"
      ></challenge-list-card>
    </div>
    <div class="align-center">
      <infinite-loading
        ref="InfiniteLoading"
        @infinite="getData"
        spinner="waveDots"
      >
        <div class="infinite-loading-message" slot="no-more">
          <b-button @click="scrollUp"
            >마지막입니다 <b-icon icon="arrow-up-circle"></b-icon
          ></b-button>
        </div>
        <div class="infinite-loading-message" slot="no-results">
          결과가 없습니다 :(
        </div>
        <div class="infinite-loading-message" slot="error">
          불러오지 못했습니다.
        </div>
      </infinite-loading>
    </div>
  </div>
</template>
<script>
import ChallengeListCard from "../components/ChallengeListCard.vue";
import SelectPreference from "../components/SelectPreference.vue";
import InfiniteLoading from "vue-infinite-loading";
import ChallengeListDummyData from "@/assets/dummyData/challengeDummyData.json";
import "@/assets/css/challengerecommend.css";
import swal from '@/assets/javascript/sweetAlert.js';
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

import { mapGetters } from "vuex";

export default {
  name: "ChallengeRecommend",
  components: { SelectPreference,ChallengeListCard,InfiniteLoading },
  data() {
    return {
      recommendCategory: 1,
      page: 1,
      challengeAllList: null,
     modalShow: false,
      didSurvey: false,
      challengeList: [],
      fit: [],
      body: [],
      day: [],
      categoryName: [
        [
          "카테고리는",
          "요가",
          "필라테스",
          "유산소",
          "댄스",
          "스트레칭",
          "근력",
          "키즈",
          "복싱",
          "식단",
          "기타",
        ],
        [
          "운동부위는",
          "전신",
          "상체",
          "하체",
          "가슴",
          "팔",
          "복부",
          "엉덩이",
          "다리",
          "기타",
        ],
        ["요일은", "월", "화", "수", "목", "금", "토", "일"]],
    };
  },
  watch: {
    recommendCategory: function() {
      this.challengeAllList = null;
     this.challengeAllList = null;
      this.challengeList = [];
      this.page = 1;
      if (this.$refs.InfiniteLoading) {
        this.$refs.InfiniteLoading.stateChanger.reset();
      }
    },
  },

  computed: {
    ...mapGetters(["getUserUid", "getUserNickName"]),
    getCategoryName: function() {
      return this.categoryName[this.recommendCategory - 1][0];
    },
 getCategoryList: function() {
      let result = [];
      let arr = [];
      switch (this.recommendCategory) {
        case 1: //fit
          arr = this.fit;
          break;
        case 2: //body
          arr = this.body;
          break;
        case 3: //day
          arr = this.day;
          break;
      }

      for (let index = 0; index < arr.length; index++) {
        result[index] =  this.categoryName[this.recommendCategory - 1][arr[index]];
      }
      return result.join(',');
    },
   
  },
methods: {
    async CheckFavoriteSurvey() {
      let result = false;
      await axios
        .get(`${SERVER_URL}/user/favorite/isSetting`, {
          params: {
            uid:this.getUserUid,
          },
        })
        .then(({ data }) => {
          result = data;
        })
        .catch(() => {
          swal.error('선호도 정보를 불러오지 못했습니다.');
      });
      return result;
    },
    async getAllData() {
      await axios
         .get(`${SERVER_URL}/challenge/recommend/${this.recommendCategory}`, {
          params: {
            uid: this.getUserUid,
          },
        })
        .then(({ data }) => {
          this.challengeAllList = data.returnList;
          this.fit = this.StringToNumberList(data.fit);
          this.day = this.StringToNumberList(data.day);
          this.body = this.StringToNumberList(data.body);
        })
        .catch(() => {
          if (!this.modalShow) {
            swal.error('챌린지 목록을 불러오지 못했습니다.');
            this.challengeAllList = ChallengeListDummyData.challengeList;
          } else {
            this.challengeAllList = [];
          }
          
        });
    },

    async getData($state) {
      if (!this.didSurvey) {
        //설문조사 했는지 판단
        this.didSurvey =  await this.CheckFavoriteSurvey();
        if(this.didSurvey){ //설문조사 했으면
          this.modalShow = false;
        }else{   //설문 안했으면
          this.modalShow = true;
          return;
        }
      }
      if (this.challengeAllList == null) {
        //챌린지 정보 불러오지 않았을 때
        await this.getAllData();
      }
      let getArray = this.challengeAllList.slice(
        (this.page - 1) * 10,
        this.page * 10
      );
      setTimeout(() => {
        if (getArray.length > 0) {
          this.challengeList = this.challengeList.concat(getArray);
          ++this.page;
          $state.loaded();
        } else {
          $state.complete();
        }
      }, 100);
    },
     StringToNumberList:function(list){
      for (let index = 0; index < list.length; index++) {
        list[index] = Number(list[index]);
      }
        return list;
    },
    ChallengeMoreInfo: function(challenge_id) {
      this.$router.push(`/challenge-more-info/${challenge_id}`);
    },
    scrollUp: function() {
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: "smooth",
      });
    },
  },
};
</script>
