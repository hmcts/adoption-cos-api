const env = process.env.ENVIRONMENT || 'aat';

interface UrlConfig {
  [key: string]: string;
}

export const urlConfig: UrlConfig = {
  idamUrl: process.env.AAT_IDAM_URL || `https://idam-api.${env}.platform.hmcts.net`,
  laPortalUrl: process.env.LA_BASE_URL || `https://adoption-web.${env}.platform.hmcts.net/la-portal/kba-case-ref/`,
};
