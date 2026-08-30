const defaultTheme = require('tailwindcss/defaultTheme');

/** @type {import('tailwindcss').Config} */
module.exports = {
  // les JS sont scannes aussi : certaines classes sont ajoutees dynamiquement (toast, boutique, palette)
  content: ['./src/main/webapp/**/*.{jsp,jspf,js}'],
  theme: {
    extend: {
      fontFamily: {
        // Inter devient la police par defaut, Fredoka (ronde, lisible) pour les titres et le logo
        sans: ['Inter', ...defaultTheme.fontFamily.sans],
        display: ['Fredoka', ...defaultTheme.fontFamily.sans],
      },
      colors: {
        // brun dore : boutons principaux, liens, accents (blanc sur 600 reste lisible)
        cookie: {
          50: '#FDF8F0',
          100: '#F9EBD3',
          200: '#F2D3A3',
          300: '#E9B76E',
          400: '#DE9A45',
          500: '#C97F2C',
          600: '#A86422',
          700: '#864E1E',
          800: '#6B3F1D',
          900: '#57341A',
        },
        cream: '#FBF5EA',
        choco: '#3B2416',
      },
      // flottement lent du cookie sur l'accueil
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-8px)' },
        },
      },
      animation: {
        float: 'float 4s ease-in-out infinite',
      },
    },
  },
  plugins: [],
};
