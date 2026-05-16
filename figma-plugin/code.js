// SchemeOnYou DB UI Mockup
// Native Figma Plugin API source generated from design/mockup/design/final-summary.md and db-diagram-ui-ux-v07.md.
// Import manifest.json in Figma: Plugins → Development → Import plugin from manifest.

const C = {
  bg: '#0F172A', app: '#0B1220', panel: '#111827', panel2: '#172033', line: '#334155',
  canvasA: '#F8FAFC', canvasB: '#EEF2FF', text: '#E5E7EB', muted: '#94A3B8',
  blue: '#2563EB', blueSoft: '#DBEAFE', green: '#22C55E', greenSoft: '#DCFCE7',
  amberSoft: '#FEF3C7', white: '#FFFFFF', slate: '#CBD5E1', darkText: '#0F172A',
  grid: '#E2E8F0', edge: '#475569'
};

function rgb(hex) {
  const s = hex.replace('#', '');
  return { r: parseInt(s.slice(0, 2), 16) / 255, g: parseInt(s.slice(2, 4), 16) / 255, b: parseInt(s.slice(4, 6), 16) / 255 };
}
function solid(hex, opacity = 1) { return { type: 'SOLID', color: rgb(hex), opacity }; }
function radius(node, r) { node.cornerRadius = r; return node; }
function place(node, x, y, w, h) { node.x = x; node.y = y; if (w != null) node.resize(w, h); return node; }
function rect(parent, name, x, y, w, h, fill, stroke, r = 0, sw = 1) {
  const n = figma.createRectangle(); n.name = name; place(n, x, y, w, h); radius(n, r);
  n.fills = fill ? [solid(fill)] : [];
  if (stroke) { n.strokes = [solid(stroke)]; n.strokeWeight = sw; }
  parent.appendChild(n); return n;
}
function line(parent, name, x1, y1, x2, y2, color = C.edge, weight = 2) {
  const n = figma.createLine(); n.name = name; n.x = x1; n.y = y1;
  const dx = x2 - x1, dy = y2 - y1;
  n.resize(Math.max(1, Math.sqrt(dx * dx + dy * dy)), 0);
  n.rotation = Math.atan2(dy, dx) * 180 / Math.PI;
  n.strokes = [solid(color)]; n.strokeWeight = weight;
  parent.appendChild(n);
  arrow(parent, name + ' arrow', x2, y2, Math.atan2(dy, dx), color);
  return n;
}
function arrow(parent, name, x, y, angle, color) {
  const p = figma.createPolygon(); p.name = name; p.pointCount = 3; place(p, x - 7, y - 7, 14, 14);
  p.rotation = angle * 180 / Math.PI + 90; p.fills = [solid(color)]; parent.appendChild(p); return p;
}
async function text(parent, name, value, x, y, w, h, size = 13, color = C.text, weight = 'ExtraLight') {
  const n = figma.createText(); n.name = name; n.x = x; n.y = y;
  n.fontName = { family: 'Monaspace Krypton', style: weight }; n.fontSize = size; n.fills = [solid(color)];
  n.characters = value; if (w != null) n.resize(w, h || n.height);
  parent.appendChild(n); return n;
}
function chip(parent, label, x, y, w, fill, color = C.darkText) {
  rect(parent, `chip/${label}`, x, y, w, 28, fill, null, 10);
  return text(parent, `chip label/${label}`, label, x + 12, y + 7, w - 24, 18, 12, color, 'Regular');
}
async function table(parent, spec) {
  rect(parent, `table/${spec.name}`, spec.x, spec.y, spec.w, spec.h, C.white, spec.selected ? C.blue : C.slate, 18, spec.selected ? 3 : 2);
  rect(parent, `table/${spec.name}/header`, spec.x, spec.y, spec.w, 46, spec.selected ? C.blueSoft : '#F1F5F9', null, 18);
  await text(parent, `table/${spec.name}/title`, spec.name, spec.x + 20, spec.y + 15, spec.w - 40, 20, 16, C.darkText, 'Regular');
  if (spec.pin) {
    rect(parent, `table/${spec.name}/pin badge`, spec.x + spec.w - 58, spec.y + 12, 40, 22, C.amberSoft, null, 11);
    await text(parent, `table/${spec.name}/pin text`, 'PIN', spec.x + spec.w - 47, spec.y + 17, 30, 12, 10, '#92400E', 'Regular');
  }
  let y = spec.y + 68;
  for (const c of spec.columns) {
    if (c.selected) rect(parent, `column selection/${spec.name}.${c.name}`, spec.x + 14, y - 12, spec.w - 28, 28, C.greenSoft, C.green, 9, 1);
    await text(parent, `column/${spec.name}.${c.name}`, c.label, spec.x + 22, y - 6, spec.w * .55, 18, 13, c.selected ? '#166534' : '#334155', 'Regular');
    await text(parent, `type/${spec.name}.${c.name}`, c.type, spec.x + spec.w - 76, y - 6, 60, 18, 13, c.selected ? '#166534' : '#334155', 'Regular');
    y += 30;
  }
  if (spec.note) {
    rect(parent, `note/${spec.name}`, spec.x + 14, spec.y + spec.h - 36, spec.w - 28, 24, '#EEF2FF', null, 8);
    await text(parent, `note text/${spec.name}`, spec.note, spec.x + 26, spec.y + spec.h - 30, spec.w - 52, 14, 11, '#3730A3', 'Regular');
  }
}

async function main() {
  await Promise.all([
    figma.loadFontAsync({ family: 'Monaspace Krypton', style: 'ExtraLight' }),
    figma.loadFontAsync({ family: 'Monaspace Krypton', style: 'Regular' })
  ]);

  const page = figma.currentPage;
  page.name = 'SchemeOnYou DB UI Mockup';

  const root = figma.createFrame(); root.name = 'SchemeOnYou DB diagram editor — native editable mockup';
  place(root, 0, 0, 1440, 1320); root.fills = [solid(C.bg)]; page.appendChild(root);

  await text(root, 'title', 'SchemeOnYou · DB diagram editor mockup', 64, 52, 900, 44, 30, '#F8FAFC', 'Regular');
  await text(root, 'subtitle', 'Keyboard-first JavaFX canvas · native editable Figma layout, not an imported SVG', 64, 88, 980, 24, 15, C.slate);

  rect(root, 'app shell', 64, 120, 1312, 820, C.app, C.line, 28);
  rect(root, 'top bar', 88, 144, 1264, 58, C.panel, '#1F2937', 16);
  rect(root, 'project tree panel', 88, 220, 210, 600, C.panel, '#253244', 18);
  rect(root, 'canvas', 316, 220, 820, 600, C.canvasA, C.slate, 20);
  rect(root, 'inspector panel', 1154, 220, 198, 600, C.panel, '#253244', 18);
  rect(root, 'footer', 88, 838, 1264, 78, C.panel, '#253244', 18);

  await text(root, 'top/app name', 'SchemeOnYou', 192, 166, 120, 20, 15, C.text, 'Regular');
  await text(root, 'top/project', 'Project: Shop schema · Diagram: DB overview · Saved', 326, 166, 520, 20, 13, C.muted);
  rect(root, 'zoom chip', 1110, 157, 102, 32, C.panel2, C.line, 10);
  await text(root, 'zoom text', 'Zoom 100%', 1135, 166, 70, 18, 12, C.muted);
  rect(root, 'space help button', 1224, 157, 102, 32, C.blue, null, 10);
  await text(root, 'space help text', 'Space help', 1242, 166, 75, 18, 12, '#EFF6FF', 'Regular');

  await text(root, 'tree title', 'Project tree', 108, 244, 150, 20, 14, C.text, 'Regular');
  await text(root, 'tree diagrams', 'DIAGRAMS', 108, 276, 80, 16, 12, C.muted, 'Regular');
  rect(root, 'active diagram row', 104, 294, 178, 34, C.blue, null, 9);
  await text(root, 'active diagram text', 'DB overview', 118, 304, 120, 18, 13, '#EFF6FF', 'Regular');
  await text(root, 'tree sequence', 'Sequence: checkout', 118, 344, 150, 18, 13, C.slate);
  await text(root, 'tree tables label', 'TABLES', 108, 390, 80, 16, 12, C.muted, 'Regular');
  for (const [i, n] of ['users', 'orders', 'roles', 'user_roles'].entries()) await text(root, `tree/${n}`, n, 118, 416 + i * 28, 120, 18, 13, C.slate);
  rect(root, 'area shortcut hint', 108, 746, 156, 42, '#0F172A', C.line, 12);
  await text(root, 'area shortcut hint text', '0/1/2/3 areas', 126, 760, 120, 18, 12, C.muted);

  // Canvas grid
  for (let y = 258; y < 780; y += 48) line(root, `grid h ${y}`, 340, y, 1110, y, C.grid, 1);
  for (let x = 364; x < 1100; x += 48) line(root, `grid v ${x}`, x, 244, x, 796, C.grid, 1);
  await text(root, 'canvas label', 'CANVAS', 340, 244, 80, 16, 12, '#64748B', 'Regular');

  // Edges before table cards
  line(root, 'fk orders.user_id to users.id', 584, 389, 714, 392, C.edge, 2.4);
  line(root, 'fk user_roles.user_id to users.id', 584, 484, 762, 583, C.edge, 2.4);
  line(root, 'fk user_roles.role_id to roles.id', 556, 618, 762, 619, C.edge, 2.4);
  line(root, 'derived relation orders to user_roles', 884, 388, 884, 560, '#64748B', 1.5);

  await table(root, { name: 'users', x: 388, y: 304, w: 196, h: 184, selected: true, pin: true, note: 'selected target users.id', columns: [
    { label: '🔑 id', name: 'id', type: 'uuid' }, { label: '◇ email', name: 'email', type: 'text!' }, { label: 'created_at', name: 'created_at', type: 'ts!' }
  ]});
  await table(root, { name: 'orders', x: 714, y: 300, w: 206, h: 186, columns: [
    { label: '🔑 id', name: 'id', type: 'uuid' }, { label: '→ user_id', name: 'user_id', type: 'uuid!', selected: true }, { label: 'total', name: 'total', type: 'decimal' }, { label: 'created_at', name: 'created_at', type: 'ts!' }
  ]});
  await table(root, { name: 'roles', x: 388, y: 560, w: 168, h: 120, columns: [
    { label: '🔑 id', name: 'id', type: 'uuid' }, { label: 'name', name: 'name', type: 'text!' }
  ]});
  await table(root, { name: 'user_roles', x: 762, y: 552, w: 218, h: 158, columns: [
    { label: '🔑→ user_id', name: 'user_id', type: 'uuid' }, { label: '🔑→ role_id', name: 'role_id', type: 'uuid' }
  ], note: 'composite PK marker'});

  // Progressive command sheet
  rect(root, 'progressive Space command sheet', 432, 704, 330, 92, '#0F172A', C.blue, 18);
  await text(root, 'sheet title', 'A · Add…', 454, 724, 120, 20, 14, C.text, 'Regular');
  await chip(root, 'T', 454, 744, 60, C.blueSoft);
  await text(root, 'sheet table', 'Table', 522, 751, 70, 18, 13, C.slate);
  await chip(root, 'F', 600, 744, 60, C.greenSoft);
  await text(root, 'sheet fk', 'Foreign key', 668, 751, 90, 18, 13, C.slate);

  // Inspector
  await text(root, 'inspector title', 'Inspector', 1174, 244, 120, 20, 14, C.text, 'Regular');
  await text(root, 'selected label', 'SELECTED', 1174, 278, 80, 16, 12, C.muted, 'Regular');
  rect(root, 'selected card', 1170, 298, 166, 76, C.panel2, C.line, 14);
  await text(root, 'selected card title', 'orders.user_id', 1186, 316, 140, 18, 13, C.text, 'Regular');
  await text(root, 'selected card subtitle', 'Column · FK source', 1186, 340, 140, 18, 12, C.muted);
  await text(root, 'fk preview label', 'FOREIGN KEY PREVIEW', 1174, 404, 150, 16, 12, C.muted, 'Regular');
  rect(root, 'fk preview card', 1170, 424, 166, 156, '#0F172A', C.line, 14);
  await text(root, 'source label', 'Source', 1186, 446, 80, 16, 12, C.slate);
  await chip(root, 'orders.user_id', 1184, 462, 126, C.greenSoft, '#166534');
  await text(root, 'target label', 'Target', 1186, 504, 80, 16, 12, C.slate);
  await chip(root, 'users.id', 1184, 520, 96, C.blueSoft, '#1D4ED8');
  await text(root, 'preview hint', 'X Swap · Enter create', 1186, 560, 140, 16, 12, C.muted);
  await text(root, 'quick actions', 'QUICK ACTIONS', 1174, 622, 120, 16, 12, C.muted, 'Regular');
  rect(root, 'create fk button', 1170, 642, 166, 36, C.blue, null, 12);
  await text(root, 'create fk text', 'Create FK', 1192, 653, 90, 16, 12, '#EFF6FF', 'Regular');
  rect(root, 'keep pinned button', 1170, 690, 166, 36, C.panel2, C.line, 12);
  await text(root, 'keep pinned text', 'Keep pinned: off', 1192, 701, 120, 16, 12, C.slate, 'Regular');

  rect(root, 'context line', 108, 854, 1224, 28, C.panel2, null, 10);
  await text(root, 'context label', 'Context:', 126, 862, 64, 16, 13, '#FDE68A', 'Regular');
  await text(root, 'context value', 'Pinned target users.id · select source column · Space A F · Space U unpin', 196, 862, 760, 16, 13, C.text);
  await text(root, 'status value', 'Status: Canvas · 0/1/2/3 areas · Space commands · F1 help', 126, 892, 720, 16, 13, C.muted);

  // Design-spec frames as separate editable Figma layers for designers
  const notes = figma.createFrame(); notes.name = 'Spec / UX summary'; place(notes, 64, 966, 980, 60); notes.fills = [solid(C.panel2)]; notes.cornerRadius = 14; root.appendChild(notes);
  await text(notes, 'notes text', 'Native Figma layout: editable frames, text, rectangles, table cards, FK lines, command sheet and inspector states. SVG is only a legacy fallback asset.', 20, 20, 930, 20, 13, C.slate);

  const shortcutFrame = figma.createFrame(); shortcutFrame.name = 'Spec / keyboard shortcuts'; place(shortcutFrame, 64, 1048, 420, 210); shortcutFrame.fills = [solid(C.panel)]; shortcutFrame.strokes = [solid(C.line)]; shortcutFrame.cornerRadius = 18; root.appendChild(shortcutFrame);
  await text(shortcutFrame, 'shortcuts title', 'Keyboard shortcuts', 22, 20, 220, 22, 16, C.text, 'Regular');
  const shortcuts = [
    ['Ctrl+Shift+P', 'Command palette'], ['0 / 1 / 2 / 3', 'Top / left / canvas / inspector'],
    ['Space A T', 'Add table'], ['Space A C', 'Add column'], ['Space A F', 'Add foreign key'],
    ['Space A J', 'Add join table'], ['Space P / U', 'Pin / unpin relation'], ['Space L D', 'Layout diagram']
  ];
  for (const [i, row] of shortcuts.entries()) {
    const y = 56 + i * 18;
    await text(shortcutFrame, `shortcut/key/${i}`, row[0], 22, y, 112, 16, 11, '#BFDBFE', 'Regular');
    await text(shortcutFrame, `shortcut/action/${i}`, row[1], 146, y, 230, 16, 11, C.slate);
  }

  const stateFrame = figma.createFrame(); stateFrame.name = 'Spec / interaction states'; place(stateFrame, 508, 1048, 420, 210); stateFrame.fills = [solid(C.panel)]; stateFrame.strokes = [solid(C.line)]; stateFrame.cornerRadius = 18; root.appendChild(stateFrame);
  await text(stateFrame, 'states title', 'Interaction states', 22, 20, 220, 22, 16, C.text, 'Regular');
  const states = [
    ['Selected table', 'blue border + active table depth'], ['Pinned target', 'PIN badge + context line'],
    ['FK source hover', 'green source chip + preview'], ['Type mismatch', 'warning in single context line'],
    ['Keep target pinned', 'explicit checkbox, default off'], ['Compound actions', 'preview + one-step undo']
  ];
  for (const [i, row] of states.entries()) {
    const y = 58 + i * 23;
    rect(stateFrame, `state marker/${i}`, 22, y + 2, 8, 8, i % 2 ? C.green : C.blue, null, 4);
    await text(stateFrame, `state/name/${i}`, row[0], 42, y, 135, 16, 11, C.text, 'Regular');
    await text(stateFrame, `state/desc/${i}`, row[1], 178, y, 220, 16, 11, C.slate);
  }

  const tokenFrame = figma.createFrame(); tokenFrame.name = 'Spec / design tokens'; place(tokenFrame, 952, 1048, 360, 210); tokenFrame.fills = [solid(C.panel)]; tokenFrame.strokes = [solid(C.line)]; tokenFrame.cornerRadius = 18; root.appendChild(tokenFrame);
  await text(tokenFrame, 'tokens title', 'Design tokens', 22, 20, 220, 22, 16, C.text, 'Regular');
  const tokens = [['Primary', C.blue], ['Canvas', C.canvasA], ['Panel', C.panel2], ['FK source', C.greenSoft], ['FK target', C.blueSoft], ['Grid', C.grid]];
  for (const [i, token] of tokens.entries()) {
    const x = 22 + (i % 2) * 160, y = 58 + Math.floor(i / 2) * 42;
    rect(tokenFrame, `token swatch/${token[0]}`, x, y, 28, 28, token[1], C.line, 8);
    await text(tokenFrame, `token label/${token[0]}`, token[0], x + 38, y + 7, 100, 14, 11, C.slate, 'Regular');
  }

  figma.viewport.scrollAndZoomIntoView([root]);
  figma.notify('SchemeOnYou DB UI mockup created as native editable Figma layers');
  figma.closePlugin();
}

main().catch(err => {
  console.error(err);
  figma.notify(`Failed to create mockup: ${err && err.message ? err.message : err}`);
  figma.closePlugin();
});
