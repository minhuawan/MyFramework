using UnityEngine;

namespace MyFramework.Runtime.Services.BusyMask
{
    public class BusyMaskService : AbstractService
    {
        private int count = 0;
        private UIBusyMask busyMask;

        public override void OnCreated()
        {
            var prefab = Resources.Load<UIBusyMask>("UIBusyMask");
            busyMask = GameObject.Instantiate(prefab);
            busyMask.gameObject.SetActive(false);
        }

        public void RetainBusy()
        {
            count++;
            if (count > 0)
            {
                if (!busyMask.gameObject.activeSelf)
                {
                    busyMask.gameObject.SetActive(true);
                }
            }
        }

        public void ReleaseBusy()
        {
            count--;
            if (count <= 0)
            {
                if (busyMask.gameObject.activeSelf)
                {
                    busyMask.gameObject.SetActive(false);
                }
            }
        }

        public override void OnDestroy()
        {
            if (busyMask != null)
            {
                GameObject.Destroy(busyMask);
                busyMask = null;
            }
        }
    }
}