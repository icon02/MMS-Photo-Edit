import packageJson from '../../../package.json';

export function getTitle(currentTitle: string, page: string): string {
  const tArr = currentTitle.split(' - ');
  const suffix = tArr[tArr.length - 1];
  return page + ' - ' + packageJson.appName;
}
