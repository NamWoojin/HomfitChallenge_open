<template>
  <div>
    <challenge-title
      :equalNickName="challenge.nick_name === getUserNickName"
      :challenge_title="challenge.challenge_title"
      :challenge_img="challenge.challenge_img"
      :day_certify_count="challenge.day_certify_count"
      :start_date="challenge.start_date"
      :end_date="challenge.end_date"
      :period="challenge.period"
      :fit_id="challenge.fit_id"
      :kind="challenge.kind"
      :daylist_string="challenge.daylist_string"
      :tagList="challenge.tagList"
      :bodyList="challenge.bodyList"
      :check_date="challenge.check_date"
      :nick_name="challenge.nick_name"
      :user_img="challenge.user_img"
      :people="challenge.people"
      :isParticipated="isParticipated"
    ></challenge-title>

    <div class="row col-12 col-lg-8 mx-auto pt-3">
      <div class="col-12 result-main">
        <!-- 나, 평균 달성률 -->
        <div>
          <div class="result-title">나의 달성률</div>
          <div class="progress mt-2 progress-box">
            <div class="progress-bar progress-background" id="my-progress" role="progressbar" style="width: 15%" :aria-valuenow="certifyInfo.user_rate" aria-valuemin="0" aria-valuemax="100">{{ this.certifyInfo.user_rate }}%</div>
          </div>
          <br>
          <div class="result-title">평균 달성률</div>
          <div class="progress mt-2 progress-box">
            <div class="progress-bar progress-background" id="avg-progress" role="progressbar" style="width: 15%" :aria-valuenow="certifyInfo.average_rate" aria-valuemin="0" aria-valuemax="100">{{ this.certifyInfo.average_rate }}%</div>
          </div>
          <br>
          <!-- 인증 -->
          <div class="row mx-auto my-1 certify-menu">
            <div class="col-4">
              <div>총 횟수</div>
              <div><span class="certify-number">{{ certifyInfo.total_cnt }} </span>개</div>
            </div>
            <div class="border-left border-right col-4">
              <div>인증 횟수</div>
              <div><span class="certify-number">{{ certifyInfo.user_cnt }} </span>개</div>
            </div>
            <div class="col-4">
              <div>남은 인증</div>
              <div><span class="certify-number">{{ certifyInfo.total_cnt - certifyInfo.user_cnt }} </span>개</div>
            </div>
          </div>
          <br>
        </div>
        <!-- 오늘한 인증 사진 -->
        <!--  -->
        <div v-if="certifyInfo.today_cnt > 0">
          <div class="result-title">오늘 찍은 인증샷</div>
          <div class="row" v-if="certifyInfo.imgList.length > 0">
            <div v-for="(item, index) in certifyInfo.imgList" class="col-12 col-sm-4" :key="index">
              <img v-if="item.register_date !== ''" :src="item.feed_picture" alt="certify" class="progress-img col-12 certify-mt px-0 mt-0">
              <img v-else :src="item.feed_picture" alt="certify" class="progress-img col-12 certify-mt certify-default-img px-0 mt-0" @click="Certify">
              <div class="certify-mt col-12">
                <div v-if="item.register_date !== ''" class="row mx-auto justify-content-between">
                  <li class="col-8 px-0 py-0 my-1">
                    <span v-if="item.register_date.slice(11,13) < 13">오전 {{ item.register_date.slice(11,13) }}시</span>
                    <span v-else>오후 {{ item.register_date.slice(11,13)-12 }}시</span>
                     {{ item.register_date.slice(14,16) }}분
                  </li>
                  <div class="row col-4 mx-0 px-0 justify-content-end">
                    <!-- <button class="btn-sm certify-btn certify-edit-btn col-5">편집</button> -->
                    <button class="btn-sm certify-btn certify-delete-btn col-5" @click="DeleteFeed(item.feed_id, index)">삭제</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 나의 후기 -->
        <div v-if="isReview === true">
          <div class="result-title">나의 후기</div>
          <review
            :review="certifyInfo.review"
          ></review>
        </div>
      </div>
    </div>
    
    <!-- 오늘 인증하는 날일때 인증버튼(수정 필요), 완료면 후기버튼 -->
    <!-- <div class="btn btn-primary col" @click="Certify">인증하기</div> -->
    <div v-if="!isMobile">
        <!-- v-if="certifyInfo.today_cnt > 0 & challenge.check_date === 1" -->
      <b-button-group
        v-if="certifyInfo.imgList != null && certifyInfo.imgList[certifyInfo.imgList.length-1].register_date == ''"
        id="info-float-button-group"
        class="row info-float align-center"
      >
        <b-button class="apply-button pc" @click="Certify">인증하기</b-button>
      </b-button-group>
      <b-button-group
        v-if="challenge.check_date === 2 & isReview === false"
        id="info-float-button-group"
        class="row info-float align-center"
      >
        <b-button class="apply-button pc" @click="Review">후기쓰기</b-button>
      </b-button-group>
    </div>
    <div v-if="isMobile">
      <div class="info-float" v-if="certifyInfo.imgList != null && certifyInfo.imgList[certifyInfo.imgList.length-1].register_date == ''">
        <div class="row info-float-inside col-12 mx-auto">
          <div class="col-12 align-center certify-btn-my">
            <b-button
              class="apply-button mobile"
              @click="Certify"
            >인증하기</b-button
            >
          </div>
        </div>
      </div>
    </div>
    <div v-if="isMobile">
      <div class="info-float" v-if="challenge.check_date === 2 & isReview === false">
        <div class="row info-float-inside col-12 mx-auto">
          <div class="col-12 align-center certify-btn-my">
            <b-button
              class="apply-button mobile"
              @click="Review"
            >후기쓰기</b-button
            >
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import ChallengeTitle from "@/components/ChallengeMoreInfo/ChallengeTitle.vue";
import Review from "@/components/ChallengeMoreInfo/Review.vue";
import swal from '@/assets/javascript/sweetAlert.js';
import '@/assets/css/ChallengeMoreInfo/challengeMoreInfo.css'
import { mapGetters } from "vuex";
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  components: {
    ChallengeTitle,
    Review,
  },
  props: {
    challenge: Object,
    isMobile: Boolean,
    certifyInfo: Object,
  },
  data() {
    return {
      isParticipated: true,
      isReview: false,
      // 더미
      // certifyInfo: {
      //   challenge: {},
      //   review: {},
      //   total_cnt: 3,
      //   today_cnt: 3,
      //   user_cnt: 1,
      //   average_rate: 90,
      //   user_rate: 80,
      //   imgList: [
      //     {
      //       feed_picture: "https://picsum.photos/300/300/?image=25",
      //       register_date: "2021.03.12",
      //     },
      //   ],
      // },
    }
  },
  created() {
    // console.log(this.certifyInfo);
    // console.log(this.getUserUid)
    // console.log(this.challenge.challenge_id)
    // axios
    //   .get(`${SERVER_URL}/challenge/detailManageMent/${this.challenge.challenge_id}/${this.getUserUid}`)
    //   .then(({ data }) => {
    //     console.log(data)
    //     // this.certifyInfo = data;
    //     // console.log(this.certifyInfo);
    //   })
    //   .catch(() => {
    //     alert("챌린지 목록을 불러오지 못했습니다.");
    //   });
    // while (this.certifyInfo.today_cnt > this.certifyInfo.imgList.length) {
    //   this.certifyInfo.imgList.push({
    //     feed_picture: "https://picsum.photos/300/300/?image=24",
    //     register_date: "",
    //   })
    // }
  },
  computed: {
    ...mapGetters(["getUserUid", "getUserNickName"]),
  },
  mounted() {
    this.ProgressBarWidth()
  },
  watch: {
    certifyInfo() {
      this.ProgressBarWidth()
      this.IsReview()
    }
  },
  methods: {
    Certify() {
      this.$router.push({name: 'CertificationPhoto', params: {uid: this.getUserUid, challenge_id: this.challenge.challenge_id, only_cam: this.challenge.only_cam}});
    },
    Review() {
      this.$router.push(`/review/${this.challenge.challenge_id}`);
    },
    ProgressBarWidth() {
      const myProgress = document.getElementById('my-progress')
      myProgress.style.width = `${this.certifyInfo.user_rate}%`
      const avgProgress = document.getElementById('avg-progress')
      avgProgress.style.width = `${this.certifyInfo.average_rate}%`
    },
    IsReview() {
      if (this.certifyInfo.review) {
        this.isReview = true
      }
    },
    DeleteFeed(feed_id, index) {
      axios
        .delete(
          `${SERVER_URL}/feed/delete`, {
            params: {
              feed_id: feed_id
            }
          }
        )
        .then(() => {
          swal.success('인증이 삭제되었습니다.');
          // this.certifyInfo.imgList[index].feed_picture = "https://picsum.photos/300/300/?image=24",
          this.certifyInfo.imgList[index].feed_picture = "https://homfitimage.s3.ap-northeast-2.amazonaws.com/e078c32b25d4a47db670fa010674a09a1613652950622",
          this.certifyInfo.imgList[index].register_date = ""
          this.certifyInfo.user_cnt--
        })
        .catch(() => {
          swal.error('오류가 발생했습니다.');
        });
    },
    dd() {
      // console.log(this.certifyInfo.imgList.length)
    }
  }
}
</script>

<style>
.progress-img{
  height: 260px;
  object-fit: fill;
}
</style>