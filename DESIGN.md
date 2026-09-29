# Kasir Saku — visual direction

- Character: calm, dependable shop tool; avoid loud gradients and decorative effects.
- Palette: forest green for primary actions, sage for selected states, warm off-white canvas, white work surfaces, dark ink text.
- Typography: bundle Inter under its SIL Open Font License for the shipped app; use system sans as the prototype fallback. Default body 14–16 sp, secondary labels 12 sp minimum, totals 20 sp or larger; use tabular numerals for money where supported.
- Controls: 48 dp minimum interactive targets; clear text labels on primary actions; preserve keyboard and screen-reader semantics.
- Icons: original rounded line drawings on a 24 dp grid, consistent 1.8 dp stroke, no emoji as interface icons. Product illustrations use the same family with restrained category tints.
- Layout: phone-first; tablet and landscape use a wider catalog plus persistent cart. Keep cashier actions reachable without relying on precise taps.
- Accessibility target: WCAG-equivalent contrast checks for text and controls, Android font scaling, TalkBack labels, and visible focus/pressed states.

The current native starter implements palette, custom line icons, touch sizing, and responsive phone/tablet layout. The Inter font file and print-specific UI are not bundled yet.
