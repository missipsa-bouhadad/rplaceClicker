/** @type {import('tailwindcss').Config} */
module.exports = {
  // les JS sont scannes aussi : certaines classes sont ajoutees dynamiquement (toast, boutique, palette)
  content: ['./src/main/webapp/**/*.{jsp,js}'],
  theme: { extend: {} },
  plugins: [],
};
