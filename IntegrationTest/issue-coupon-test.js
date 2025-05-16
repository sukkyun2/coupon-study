import http from 'k6/http';
import {check} from 'k6';

const VUS = 100;
const ITERATIONS = 2;

export let options = {
    scenarios: {
        coupon_issue: {
            executor: 'per-vu-iterations',
            vus: VUS,
            iterations: ITERATIONS,
            maxDuration: '2m',
        },
    },
    thresholds: {
        http_req_failed: ['rate < 0.01'],
        http_req_duration: ['p(95) < 500'],
    },
};


export function setup() {
    const res = createCoupon();

    check(res, {'쿠폰 생성 성공': isApiResponseOk});

    return {couponId: res.json().data};
}

export default function ({couponId}) {
    const userId = getNextUserId();
    const res = http.post(`http://localhost:8080/api/v1/coupons/${couponId}/issue?userId=${userId}`, null, {headers: {'Content-Type': 'application/json'}});

    const ok = check(res, {'쿠폰 발급 성공': isApiResponseOk});

    if (!ok) {
        console.error(`❌ 실패 응답: status=${res.status}, body=${res.body} userId=${userId}`);
    }
}


function createCoupon() {
    const createPayload = JSON.stringify({
        name: "부하테스트 쿠폰",
        amount: 10000,
        couponType: "CHICKEN",
        expiredAt: "2025-12-31 23:59:59"
    });

    return http.post('http://localhost:8080/api/v1/coupons', createPayload, {headers: {'Content-Type': 'application/json'}});
}

function getNextUserId() {
    const idx = (__ITER * VUS) + (__VU - 1);
    return idx + 1;
}

function isApiResponseOk(res) {
    try {
        return res.json().code === '200';
    } catch (e) {
        return false;
    }
}
