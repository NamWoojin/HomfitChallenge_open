<template>
  <div class="col-12 col-md-8 bookmark-container">
    <p class="bookmark-title">챌린지 북마크</p>
    <div class="row bookmark-list-card">
      <ChallengeListCard
        v-for="(challenge, index) in challengeList"
        :key="`${index}_bookmark`"
        class="col-6 col-md-4 mb-2 bookmark-challenge-list-card"
        :challenge="challenge"
        :isfromBookmark="isfromBookmark"
        @moreInfo="ChallengeMoreInfo"
        @deleteBookmarkBtn="DeleteBookmark"
      />
    </div>
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
        북마크된 챌린지가 없습니다.
      </div>
      <div class="infinite-loading-message" slot="error">
        불러오지 못했습니다.
      </div>
    </infinite-loading>
  </div>
</template>

<script>
const dayList = ["", "월", "화", "수", "목", "금", "토", "일"];
import { mapGetters } from "vuex";
import axios from "axios";
import ChallengeListCard from "../components/ChallengeListCard.vue";
import InfiniteLoading from "vue-infinite-loading";
import "@/assets/css/infiniteloading.css";
import swal from "@/assets/javascript/sweetAlert.js";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  components: {
    ChallengeListCard,
    InfiniteLoading,
  },
  data() {
    return {
      page: 1,
      isfromBookmark: 1,
      challengeAllList: null,
      challengeList: [
        // 더미 데이터 (DB 없을 때 확인용)
        // { "challenge_id": 134, "challenge_title": "조싀앤바믜 마성의 링딩동 챌린지", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 4, "check_date": 0, "period": 8, "nick_name": "이건내닉네임이얌", "people": 4, "kind": 0, "daylist_string": "[2, 4]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 132, "challenge_title": "힙으뜸 8분 힙운동 따라하기", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 6, "check_date": 0, "period": 28, "nick_name": "건강이", "people": 3, "kind": 0, "daylist_string": "[3, 5, 7]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 131, "challenge_title": "키쑥쑥 성장운동", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 7, "check_date": 0, "period": 7, "nick_name": "건강이", "people": 3, "kind": 0, "daylist_string": "[2, 4, 6]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 128, "challenge_title": "기초체력 홈트 15분 루틴", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 3, "check_date": 0, "period": 7, "nick_name": "우지니", "people": 3, "kind": 0, "daylist_string": "[1, 2, 3, 4, 5]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 183, "challenge_title": "원푸드 다이어트", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 9, "check_date": 0, "period": 11, "nick_name": "관리자3", "people": 2, "kind": 0, "daylist_string": "[1, 3, 5]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 179, "challenge_title": "sad", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 3, "check_date": 0, "period": 29, "nick_name": "loco", "people": 2, "kind": 0, "daylist_string": "[4]", "dayList": null, "tagList": null, "bodyList": null },
        // { "challenge_id": 178, "challenge_title": "챌린지개설", "challenge_contents": null, "challenge_img": "", "challenge_certify_contents": null, "good_img": null, "bad_img": null, "day_certify_count": 0, "only_cam": 0, "start_date": null, "end_date": null, "make_date": null, "make_uid": null, "fit_id": 9, "check_date": 0, "period": 3, "nick_name": "닉네임", "people": 2, "kind": 0, "daylist_string": "[4]", "dayList": null, "tagList": null, "bodyList": null },
      ],
    };
  },
  computed: {
    ...mapGetters(["getUserUid"]),
  },
  methods: {
    ChallengeMoreInfo(challenge_id) {
      this.$router.push(`/challenge-more-info/${challenge_id}`);
    },
    GetDayList(challenge) {
      if (challenge.daylist_string != null) {
        let list = challenge.daylist_string
          .substring(1, challenge.daylist_string.length - 1)
          .split(",");
        let temp = new Array(list.length);
        for (let index = 0; index < list.length; index++) {
          temp[index] = dayList[parseInt(list[index].replace(" ", ""))];
        }
        return temp.join("/");
      }
      return "";
    },
    DeleteBookmark(challenge_id) {
      axios
        .delete(`${SERVER_URL}/user/bookmark/${challenge_id}`, {
          params: {
            uid: this.getUserUid,
          },
        })
        .then(() => {
          swal.success("북마크가 해제되었습니다.");
          const itemToFind = this.challengeList.find(function(item) {
            return item.challenge_id === challenge_id;
          });
          const idx = this.challengeList.indexOf(itemToFind);
          if (idx > -1) this.challengeList.splice(idx, 1);
          if (this.challengeList.length == 0) this.getNewData();
        })
        .catch(() => {
          swal.error("오류가 발생했습니다.");
        });
    },
    getNewData: function() {
      this.page = 1;
      this.challengeAllList = null;
      this.challengeList = [];
      if (this.$refs.InfiniteLoading) {
        this.$refs.InfiniteLoading.stateChanger.reset();
      }
    },
    async getAllData() {
      await axios
        .get(`${SERVER_URL}/challenge/bookmark`, {
          params: {
            uid: this.getUserUid,
          },
        })
        .then((data) => {
          this.challengeAllList = data.data;
        })
        .catch(() => {
          swal.error("챌린지 목록을 불러오지 못했습니다.");
        });
    },
    async getData($state) {
      if (this.challengeAllList == null) {
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

<style>
.bookmark-container {
  margin: 25px auto 0;
}
.bookmark-title {
  margin-left: 5px;
  padding-bottom: 5px;
  font-weight: 700;
  font-size: 1.5rem;
  border-bottom: 2px solid #eac03a;
  display: inline;
}
.bookmark-list-card {
  clear: both;
  padding-top: 15px;
  display: flex;
}

.bookmark-challenge-list-card {
  padding: 4px 4px !important;
  transition: transform 200ms ease 100ms;
}

.bookmark-btn {
  color: #eac03a;
  position: absolute;
  width: 1em;
  height: 1em;
  z-index: 4;
  left: 18px;
  top: 18px;
}
.bookmark-btn:hover {
  cursor: pointer;
}
</style>
