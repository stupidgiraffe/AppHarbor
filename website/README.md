# AppHarbor website

The static landing page for AppHarbor lives in this directory. It uses plain
HTML and CSS with no framework or build step.

The homepage is intentionally product-first: it explains what AppHarbor does,
which Android app sources it supports, the current development status, and
where to download the latest development APK.

## Preview locally

```sh
cd website
python3 -m http.server 8765
```

Then open `http://127.0.0.1:8765`.

## Deployment

The repository already contains `.github/workflows/deploy_website.yml`, but
automatic deployment is paused until GitHub Pages is enabled once for this fork.

In GitHub, open **Settings → Pages** and set the build source to
**GitHub Actions**. After that, the deployment workflow can be switched back to
automatic deployment on changes under `website/`.

## Design

The landing page keeps the inherited aurora/glass visual language while
presenting AppHarbor's own product identity. It deliberately uses a stylized
library preview rather than old Omnify screenshots; fresh AppHarbor screenshots
can replace it once the visual rebrand is complete.
